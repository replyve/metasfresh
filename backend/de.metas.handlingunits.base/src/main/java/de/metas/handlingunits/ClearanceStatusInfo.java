/*
 * #%L
 * de.metas.handlingunits.base
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

package de.metas.handlingunits;

import de.metas.handlingunits.model.I_M_HU;
import de.metas.organization.InstantAndOrgId;
import de.metas.organization.OrgId;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.compiere.util.TimeUtil;

import javax.annotation.Nullable;
import java.time.Instant;

@Builder
@Value
public class ClearanceStatusInfo
{
	@NonNull
	ClearanceStatus clearanceStatus;

	@Nullable
	String clearanceNote;

	@Nullable
	InstantAndOrgId clearanceDate;

	@Nullable
	public static ClearanceStatusInfo ofHU(@NonNull final I_M_HU hu)
	{
		return ClearanceStatus.ofCodeOptional(hu.getClearanceStatus())
				.map(status -> ClearanceStatusInfo.builder()
						.clearanceDate(InstantAndOrgId.ofTimestamp(hu.getClearanceDate(), OrgId.ofRepoId(hu.getAD_Org_ID())))
						.clearanceNote(hu.getClearanceNote())
						.clearanceStatus(status)
						.build())
				.orElse(null);
	}
}
