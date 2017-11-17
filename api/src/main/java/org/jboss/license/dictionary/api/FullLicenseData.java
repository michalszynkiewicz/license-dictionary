package org.jboss.license.dictionary.api;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 11/14/17
 */
public class FullLicenseData extends License {
    private String fedoraAbbrevation;
    private String fedoraName;
    private String spdxName;
    private String spdxAbbreviation;
    private String spdxUrl;

    public String getFedoraAbbrevation() {
        return fedoraAbbrevation;
    }

    public void setFedoraAbbrevation(String fedoraAbbrevation) {
        this.fedoraAbbrevation = fedoraAbbrevation;
    }

    public String getFedoraName() {
        return fedoraName;
    }

    public void setFedoraName(String fedoraName) {
        this.fedoraName = fedoraName;
    }

    public String getSpdxName() {
        return spdxName;
    }

    public void setSpdxName(String spdxName) {
        this.spdxName = spdxName;
    }

    public String getSpdxAbbreviation() {
        return spdxAbbreviation;
    }

    public void setSpdxAbbreviation(String spdxAbbreviation) {
        this.spdxAbbreviation = spdxAbbreviation;
    }

    public String getSpdxUrl() {
        return spdxUrl;
    }

    public void setSpdxUrl(String spdxUrl) {
        this.spdxUrl = spdxUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FullLicenseData that = (FullLicenseData) o;

        if (fedoraAbbrevation != null ? !fedoraAbbrevation.equals(that.fedoraAbbrevation) : that.fedoraAbbrevation != null)
            return false;
        if (fedoraName != null ? !fedoraName.equals(that.fedoraName) : that.fedoraName != null) return false;
        if (spdxName != null ? !spdxName.equals(that.spdxName) : that.spdxName != null) return false;
        if (spdxAbbreviation != null ? !spdxAbbreviation.equals(that.spdxAbbreviation) : that.spdxAbbreviation != null)
            return false;
        if (spdxUrl != null ? !spdxUrl.equals(that.spdxUrl) : that.spdxUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (fedoraAbbrevation != null ? fedoraAbbrevation.hashCode() : 0);
        result = 31 * result + (fedoraName != null ? fedoraName.hashCode() : 0);
        result = 31 * result + (spdxName != null ? spdxName.hashCode() : 0);
        result = 31 * result + (spdxAbbreviation != null ? spdxAbbreviation.hashCode() : 0);
        result = 31 * result + (spdxUrl != null ? spdxUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FullLicenseData{" +
                "super= " + super.toString() +
                ", fedoraAbbrevation='" + fedoraAbbrevation + '\'' +
                ", fedoraName='" + fedoraName + '\'' +
                ", spdxName='" + spdxName + '\'' +
                ", spdxAbbreviation='" + spdxAbbreviation + '\'' +
                ", spdxUrl='" + spdxUrl + '\'' +
                '}';
    }
}
