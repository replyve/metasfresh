//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.02 at 05:21:46 PM CET 
//


package de.metas.edi.esb.jaxb.metasfresh;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for EDI_Exp_Desadv_Pack_ItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EDI_Exp_Desadv_Pack_ItemType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BestBeforeDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="LotNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="QtyCU" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="QtyCUsPerLU" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="QtyTU" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="EDI_DesadvLine_ID" type="{}EDI_Exp_DesadvLineType" minOccurs="0"/&gt;
 *         &lt;element name="GTIN_TU_PackingMaterial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="M_HU_PackagingCode_TU_Text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EDI_Exp_Desadv_Pack_ItemType", propOrder = {
    "bestBeforeDate",
    "lotNumber",
    "qtyCU",
    "qtyCUsPerLU",
    "qtyTU",
    "ediDesadvLineID",
    "gtintuPackingMaterial",
    "mhuPackagingCodeTUText"
})
public class EDIExpDesadvPackItemType {

    @XmlElement(name = "BestBeforeDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bestBeforeDate;
    @XmlElement(name = "LotNumber")
    protected String lotNumber;
    @XmlElement(name = "QtyCU")
    protected BigDecimal qtyCU;
    @XmlElement(name = "QtyCUsPerLU")
    protected BigDecimal qtyCUsPerLU;
    @XmlElement(name = "QtyTU")
    protected BigInteger qtyTU;
    @XmlElement(name = "EDI_DesadvLine_ID")
    protected EDIExpDesadvLineType ediDesadvLineID;
    @XmlElement(name = "GTIN_TU_PackingMaterial")
    protected String gtintuPackingMaterial;
    @XmlElement(name = "M_HU_PackagingCode_TU_Text")
    protected String mhuPackagingCodeTUText;

    /**
     * Gets the value of the bestBeforeDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * Sets the value of the bestBeforeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBestBeforeDate(XMLGregorianCalendar value) {
        this.bestBeforeDate = value;
    }

    /**
     * Gets the value of the lotNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * Sets the value of the lotNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotNumber(String value) {
        this.lotNumber = value;
    }

    /**
     * Gets the value of the qtyCU property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQtyCU() {
        return qtyCU;
    }

    /**
     * Sets the value of the qtyCU property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQtyCU(BigDecimal value) {
        this.qtyCU = value;
    }

    /**
     * Gets the value of the qtyCUsPerLU property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQtyCUsPerLU() {
        return qtyCUsPerLU;
    }

    /**
     * Sets the value of the qtyCUsPerLU property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQtyCUsPerLU(BigDecimal value) {
        this.qtyCUsPerLU = value;
    }

    /**
     * Gets the value of the qtyTU property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQtyTU() {
        return qtyTU;
    }

    /**
     * Sets the value of the qtyTU property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQtyTU(BigInteger value) {
        this.qtyTU = value;
    }

    /**
     * Gets the value of the ediDesadvLineID property.
     * 
     * @return
     *     possible object is
     *     {@link EDIExpDesadvLineType }
     *     
     */
    public EDIExpDesadvLineType getEDIDesadvLineID() {
        return ediDesadvLineID;
    }

    /**
     * Sets the value of the ediDesadvLineID property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDIExpDesadvLineType }
     *     
     */
    public void setEDIDesadvLineID(EDIExpDesadvLineType value) {
        this.ediDesadvLineID = value;
    }

    /**
     * Gets the value of the gtintuPackingMaterial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGTINTUPackingMaterial() {
        return gtintuPackingMaterial;
    }

    /**
     * Sets the value of the gtintuPackingMaterial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGTINTUPackingMaterial(String value) {
        this.gtintuPackingMaterial = value;
    }

    /**
     * Gets the value of the mhuPackagingCodeTUText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMHUPackagingCodeTUText() {
        return mhuPackagingCodeTUText;
    }

    /**
     * Sets the value of the mhuPackagingCodeTUText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMHUPackagingCodeTUText(String value) {
        this.mhuPackagingCodeTUText = value;
    }

}
