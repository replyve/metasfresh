/*
 * #%L
 * de.metas.swat.base
 * %%
 * Copyright (C) 2022 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package de.metas.deliveryplanning;

import de.metas.bpartner.BPartnerId;
import de.metas.bpartner.BPartnerLocationId;
import de.metas.i18n.AdMessageKey;
import de.metas.incoterms.IncotermsId;
import de.metas.inout.ShipmentScheduleId;
import de.metas.inoutcandidate.ReceiptScheduleId;
import de.metas.inoutcandidate.api.IReceiptScheduleBL;
import de.metas.inoutcandidate.api.IShipmentScheduleBL;
import de.metas.inoutcandidate.model.I_M_ReceiptSchedule;
import de.metas.inoutcandidate.model.I_M_ShipmentSchedule;
import de.metas.location.CountryId;
import de.metas.order.OrderId;
import de.metas.order.OrderLineId;
import de.metas.organization.ClientAndOrgId;
import de.metas.organization.OrgId;
import de.metas.product.IProductBL;
import de.metas.product.ProductId;
import de.metas.quantity.Quantity;
import de.metas.quantity.Quantitys;
import de.metas.sectionCode.SectionCodeId;
import de.metas.shipping.model.ShipperTransportationId;
import de.metas.uom.IUOMDAO;
import de.metas.uom.UomId;
import de.metas.util.Check;
import de.metas.util.Services;
import lombok.NonNull;
import org.adempiere.ad.dao.IQueryFilter;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.service.ClientId;
import org.adempiere.service.ISysConfigBL;
import org.adempiere.warehouse.WarehouseId;
import org.compiere.model.I_C_UOM;
import org.compiere.model.I_M_Delivery_Planning;
import org.compiere.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DeliveryPlanningService
{

	public static final AdMessageKey MSG_M_Delivery_Planning_AllClosed = AdMessageKey.of("de.metas.deliveryplanning.DeliveryPlanningService.AllClosed");

	public static final AdMessageKey MSG_M_Delivery_Planning_AllOpen = AdMessageKey.of("de.metas.deliveryplanning.DeliveryPlanningService.AllOpen");

	private static final String SYSCONFIG_M_Delivery_Planning_CreateAutomatically = "de.metas.deliveryplanning.DeliveryPlanningService.M_Delivery_Planning_CreateAutomatically";
	private static final AdMessageKey MSG_M_Delivery_Planning_AtLeastOnePerOrderLine = AdMessageKey.of("M_Delivery_Planning_AtLeastOnePerOrderLine");

	public static final String PARAM_AdditionalLines = "AdditionalLines";
	private final ISysConfigBL sysConfigBL = Services.get(ISysConfigBL.class);
	private final IUOMDAO uomDAO = Services.get(IUOMDAO.class);
	private final IProductBL productBL = Services.get(IProductBL.class);
	private final IShipmentScheduleBL shipmentScheduleBL = Services.get(IShipmentScheduleBL.class);
	private final IReceiptScheduleBL receiptScheduleBL = Services.get(IReceiptScheduleBL.class);
	private final DeliveryPlanningRepository deliveryPlanningRepository;

	public DeliveryPlanningService(@NonNull final DeliveryPlanningRepository deliveryPlanningRepository)
	{
		this.deliveryPlanningRepository = deliveryPlanningRepository;
	}

	public boolean isAutoCreateEnabled(@NonNull final ClientAndOrgId clientAndOrgId)
	{
		return sysConfigBL.getBooleanValue(SYSCONFIG_M_Delivery_Planning_CreateAutomatically, false, clientAndOrgId);
	}

	public void generateIncomingDeliveryPlanning(final I_M_ReceiptSchedule receiptScheduleRecord)
	{
		final DeliveryPlanningCreateRequest deliveryPlanningRequest = receiptScheduleBL.createDeliveryPlanningRequest(receiptScheduleRecord);
		deliveryPlanningRepository.generateDeliveryPlanning(deliveryPlanningRequest);
	}

	public void generateOutgoingDeliveryPlanning(final I_M_ShipmentSchedule shipmentScheduleRecord)
	{
		final DeliveryPlanningCreateRequest deliveryPlanningRequest = shipmentScheduleBL.createDeliveryPlanningRequest(shipmentScheduleRecord);
		deliveryPlanningRepository.generateDeliveryPlanning(deliveryPlanningRequest);
	}

	public void validateDeletion(final I_M_Delivery_Planning deliveryPlanning)
	{
		final OrderLineId orderLineId = OrderLineId.ofRepoIdOrNull(deliveryPlanning.getC_OrderLine_ID());
		if (orderLineId == null)
		{
			// nothing to do: delivery planning is not based on any order line
			return;
		}

		final boolean otherDeliveryPlanningsExistForOrderLine = deliveryPlanningRepository.isOtherDeliveryPlanningsExistForOrderLine(orderLineId, DeliveryPlanningId.ofRepoId(deliveryPlanning.getM_Delivery_Planning_ID()));

		if (!otherDeliveryPlanningsExistForOrderLine)
		{
			throw new AdempiereException(MSG_M_Delivery_Planning_AtLeastOnePerOrderLine);
		}
	}

	private DeliveryPlanningCreateRequest createRequest(@NonNull final DeliveryPlanningId deliveryPlanningId, @NonNull final Quantity plannedLoadedQty)
	{
		final I_M_Delivery_Planning deliveryPlanningRecord = deliveryPlanningRepository.getById(deliveryPlanningId);
		final OrgId orgId = OrgId.ofRepoId(deliveryPlanningRecord.getAD_Org_ID());

		final ProductId productId = ProductId.ofRepoId(deliveryPlanningRecord.getM_Product_ID());
		final I_C_UOM uomOfRecord = uomDAO.getByIdOrNull(deliveryPlanningRecord.getC_UOM_ID());
		final I_C_UOM uomToUse = uomOfRecord != null ? uomOfRecord : productBL.getStockUOM(productId);

		return DeliveryPlanningCreateRequest.builder()
				.orgId(orgId)
				.clientId(ClientId.ofRepoId(deliveryPlanningRecord.getAD_Client_ID()))
				.shipmentScheduleId(ShipmentScheduleId.ofRepoIdOrNull(deliveryPlanningRecord.getM_ShipmentSchedule_ID()))
				.receiptScheduleId(ReceiptScheduleId.ofRepoIdOrNull(deliveryPlanningRecord.getM_ReceiptSchedule_ID()))
				.orderId(OrderId.ofRepoIdOrNull(deliveryPlanningRecord.getC_Order_ID()))
				.orderLineId(OrderLineId.ofRepoIdOrNull(deliveryPlanningRecord.getC_OrderLine_ID()))
				.productId(productId)
				.partnerId(BPartnerId.ofRepoId(deliveryPlanningRecord.getC_BPartner_ID()))
				.bPartnerLocationId(BPartnerLocationId.ofRepoId(deliveryPlanningRecord.getC_BPartner_ID(), deliveryPlanningRecord.getC_BPartner_Location_ID()))
				.shipperTransportationId(ShipperTransportationId.ofRepoIdOrNull(deliveryPlanningRecord.getM_ShipperTransportation_ID()))
				.incotermsId(IncotermsId.ofRepoIdOrNull(deliveryPlanningRecord.getC_Incoterms_ID()))
				.sectionCodeId(SectionCodeId.ofRepoIdOrNull(deliveryPlanningRecord.getM_SectionCode_ID()))
				.warehouseId(WarehouseId.ofRepoId(deliveryPlanningRecord.getM_Warehouse_ID()))
				.deliveryPlanningType(DeliveryPlanningType.ofNullableCode(deliveryPlanningRecord.getM_Delivery_Planning_Type()))
				.orderStatus(OrderStatus.ofNullableCode(deliveryPlanningRecord.getOrderStatus()))
				.meansOfTransportationId(MeansOfTransportationId.ofRepoIdOrNull(deliveryPlanningRecord.getM_MeansOfTransportation_ID()))
				.isB2B(deliveryPlanningRecord.isB2B())
				.qtyOrdered(Quantity.of(deliveryPlanningRecord.getQtyOrdered(), uomToUse))
				.qtyTotalOpen(Quantity.of(deliveryPlanningRecord.getQtyTotalOpen(), uomToUse))
				.actualLoadedQty(Quantity.of(deliveryPlanningRecord.getActualLoadQty(), uomToUse))
				.actualDeliveredQty(Quantity.of(deliveryPlanningRecord.getActualDeliveredQty(), uomToUse))

				.plannedLoadedQty(plannedLoadedQty)
				.plannedDischargeQty(Quantity.of(deliveryPlanningRecord.getPlannedDischargeQuantity(), uomToUse))
				.actualDischargeQty(Quantity.of(deliveryPlanningRecord.getActualDischargeQuantity(), uomToUse))

				.uom(uomToUse)
				.plannedLoadingDate(TimeUtil.asInstant(deliveryPlanningRecord.getPlannedLoadingDate()))
				.actualLoadingDate(TimeUtil.asInstant(deliveryPlanningRecord.getActualLoadingDate()))
				.plannedDeliveryDate(TimeUtil.asInstant(deliveryPlanningRecord.getPlannedDeliveryDate()))
				.actualDeliveryDate(TimeUtil.asInstant(deliveryPlanningRecord.getActualDeliveryDate()))
				.releaseNo(deliveryPlanningRecord.getReleaseNo())
				.wayBillNo(deliveryPlanningRecord.getWayBillNo())
				.batch(deliveryPlanningRecord.getBatch())
				.originCountryId(CountryId.ofRepoIdOrNull(deliveryPlanningRecord.getC_OriginCountry_ID()))
				.destinationCountryId(CountryId.ofRepoIdOrNull(deliveryPlanningRecord.getC_DestinationCountry_ID()))
				.forwarderId(ForwarderId.ofRepoIdOrNull(deliveryPlanningRecord.getM_Forwarder_ID()))
				.transportDetails(deliveryPlanningRecord.getTransportDetails())
				.build();
	}

	public void createAdditionalDeliveryPlannings(@NonNull final DeliveryPlanningId deliveryPlanningId, final int additionalLines)
	{
		Check.assumeGreaterThanZero(additionalLines, PARAM_AdditionalLines);

		final Quantity openQty = getOpenQty(deliveryPlanningId);

		final Quantity fraction = openQty.divide(BigDecimal.valueOf(additionalLines), 0, RoundingMode.DOWN);

		final Quantity remainder = openQty.subtract(fraction.multiply(additionalLines));
		final DeliveryPlanningCreateRequest initialRequest = createRequest(deliveryPlanningId, fraction.add(remainder));
		deliveryPlanningRepository.generateDeliveryPlanning(initialRequest);

		for (int i = 1; i < additionalLines; i++)
		{
			final DeliveryPlanningCreateRequest request = createRequest(deliveryPlanningId, fraction);

			deliveryPlanningRepository.generateDeliveryPlanning(request);
		}
	}

	private Quantity getOpenQty(final DeliveryPlanningId deliveryPlanningId)
	{
		final I_M_Delivery_Planning deliveryPlanningRecord = deliveryPlanningRepository.getById(deliveryPlanningId);
		final I_C_UOM uom = uomDAO.getById(deliveryPlanningRecord.getC_UOM_ID());

		final Quantity qtyOrdered = Quantity.of(deliveryPlanningRecord.getQtyOrdered(), uom);

		final OrderLineId orderLineId = OrderLineId.ofRepoIdOrNull(deliveryPlanningRecord.getC_OrderLine_ID());
		if (orderLineId == null)
		{
			// the delivery planning has no order line => remaining open qty is 0
			return Quantity.zero(uom);
		}

		Quantity openQty = qtyOrdered;

		final Quantity plannedLoadedQtySum = deliveryPlanningRepository.retrieveForOrderLine(orderLineId)
				.map(DeliveryPlanningService::extractPlannedLoadedQuantity)
				.reduce(Quantity::add)
				.orElse(null);
		if(plannedLoadedQtySum != null && !plannedLoadedQtySum.isZero())
		{
			openQty = openQty.subtract(plannedLoadedQtySum);
		}

		return openQty.toZeroIfNegative();
	}

	private static Quantity extractPlannedLoadedQuantity(final I_M_Delivery_Planning deliveryPlanning)
	{
		final UomId uomId = UomId.ofRepoId(deliveryPlanning.getC_UOM_ID());
		return Quantitys.create(deliveryPlanning.getPlannedLoadedQuantity(), uomId);
	}

	public void deleteForReceiptSchedule(@NonNull final ReceiptScheduleId receiptScheduleId)
	{
		deliveryPlanningRepository.deleteForReceiptSchedule(receiptScheduleId);
	}

	public void deleteForShipmentSchedule(@NonNull final ShipmentScheduleId shipmentScheduleId)
	{
		deliveryPlanningRepository.deleteForShipmentSchedule(shipmentScheduleId);
	}

	public boolean isClosed(final DeliveryPlanningId deliveryPlanningId)
	{
		final I_M_Delivery_Planning deliveryPlanningRecord = deliveryPlanningRepository.getById(deliveryPlanningId);
		return deliveryPlanningRecord.isClosed();
	}

	public void closeSelectedDeliveryPlannings(@NonNull final IQueryFilter<I_M_Delivery_Planning> selectedDeliveryPlanningsFilter)
	{
		deliveryPlanningRepository.closeSelectedDeliveryPlannings(selectedDeliveryPlanningsFilter);
	}

	public void reOpenSelectedDeliveryPlannings(@NonNull final IQueryFilter<I_M_Delivery_Planning> selectedDeliveryPlanningsFilter)
	{
		deliveryPlanningRepository.reOpenSelectedDeliveryPlannings(selectedDeliveryPlanningsFilter);
	}

	public boolean isExistsClosedDeliveryPlannings(@NonNull final IQueryFilter<I_M_Delivery_Planning> selectedDeliveryPlanningsFilter)
	{
		return deliveryPlanningRepository.isExistsClosedDeliveryPlannings(selectedDeliveryPlanningsFilter);
	}

	public boolean isExistsOpenDeliveryPlannings(@NonNull final IQueryFilter<I_M_Delivery_Planning> selectedDeliveryPlanningsFilter)
	{
		return deliveryPlanningRepository.isExistsOpenDeliveryPlannings(selectedDeliveryPlanningsFilter);
	}
}
