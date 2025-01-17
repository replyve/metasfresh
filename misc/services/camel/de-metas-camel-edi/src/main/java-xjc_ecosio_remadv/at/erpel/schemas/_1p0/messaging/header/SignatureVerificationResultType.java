//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.12 at 10:38:39 AM CET 
//


package at.erpel.schemas._1p0.messaging.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SignatureVerificationResultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignatureVerificationResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/messaging/header}SignatureCheck"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/messaging/header}CertificateCheck"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/messaging/header}SignatureManifestCheck"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignatureVerificationResultType", propOrder = {
    "signatureCheck",
    "certificateCheck",
    "signatureManifestCheck"
})
public class SignatureVerificationResultType {

    @XmlElement(name = "SignatureCheck")
    protected int signatureCheck;
    @XmlElement(name = "CertificateCheck")
    protected int certificateCheck;
    @XmlElement(name = "SignatureManifestCheck")
    protected int signatureManifestCheck;

    /**
     * Gets the value of the signatureCheck property.
     * 
     */
    public int getSignatureCheck() {
        return signatureCheck;
    }

    /**
     * Sets the value of the signatureCheck property.
     * 
     */
    public void setSignatureCheck(int value) {
        this.signatureCheck = value;
    }

    /**
     * Gets the value of the certificateCheck property.
     * 
     */
    public int getCertificateCheck() {
        return certificateCheck;
    }

    /**
     * Sets the value of the certificateCheck property.
     * 
     */
    public void setCertificateCheck(int value) {
        this.certificateCheck = value;
    }

    /**
     * Gets the value of the signatureManifestCheck property.
     * 
     */
    public int getSignatureManifestCheck() {
        return signatureManifestCheck;
    }

    /**
     * Sets the value of the signatureManifestCheck property.
     * 
     */
    public void setSignatureManifestCheck(int value) {
        this.signatureManifestCheck = value;
    }

}
