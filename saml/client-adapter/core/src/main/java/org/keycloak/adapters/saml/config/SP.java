package org.keycloak.adapters.saml.config;

import org.keycloak.adapters.saml.SamlDeployment;
import org.keycloak.enums.SslRequired;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class SP implements Serializable {
    public static class PrincipalNameMapping implements Serializable {
        private String policy;
        private String attributeName;

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }
    }

    private String entityID;
    private String sslPolicy;
    private boolean forceAuthentication;
    private String logoutPage;
    private List<Key> keys;
    private String nameIDPolicyFormat;
    private PrincipalNameMapping principalNameMapping;
    private Set<String> roleAttributes;
    private Set<String> roleFriendlyAttributes;
    private String signatureAlgorithm;
    private String signatureCanonicalizationMethod;
    private IDP idp;

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getSslPolicy() {
        return sslPolicy;
    }

    public void setSslPolicy(String sslPolicy) {
        this.sslPolicy = sslPolicy;
    }

    public boolean isForceAuthentication() {
        return forceAuthentication;
    }

    public void setForceAuthentication(boolean forceAuthentication) {
        this.forceAuthentication = forceAuthentication;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public String getNameIDPolicyFormat() {
        return nameIDPolicyFormat;
    }

    public void setNameIDPolicyFormat(String nameIDPolicyFormat) {
        this.nameIDPolicyFormat = nameIDPolicyFormat;
    }

    public PrincipalNameMapping getPrincipalNameMapping() {
        return principalNameMapping;
    }

    public void setPrincipalNameMapping(PrincipalNameMapping principalNameMapping) {
        this.principalNameMapping = principalNameMapping;
    }

    public Set<String> getRoleAttributes() {
        return roleAttributes;
    }

    public void setRoleAttributes(Set<String> roleAttributes) {
        this.roleAttributes = roleAttributes;
    }

    public Set<String> getRoleFriendlyAttributes() {
        return roleFriendlyAttributes;
    }

    public void setRoleFriendlyAttributes(Set<String> roleFriendlyAttributes) {
        this.roleFriendlyAttributes = roleFriendlyAttributes;
    }

    public IDP getIdp() {
        return idp;
    }

    public void setIdp(IDP idp) {
        this.idp = idp;
    }

    public String getLogoutPage() {
        return logoutPage;
    }

    public void setLogoutPage(String logoutPage) {
        this.logoutPage = logoutPage;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getSignatureCanonicalizationMethod() {
        return signatureCanonicalizationMethod;
    }

    public void setSignatureCanonicalizationMethod(String signatureCanonicalizationMethod) {
        this.signatureCanonicalizationMethod = signatureCanonicalizationMethod;
    }
}
