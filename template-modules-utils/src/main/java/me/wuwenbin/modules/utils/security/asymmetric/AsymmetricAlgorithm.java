package me.wuwenbin.modules.utils.security.asymmetric;

/**
 * 非对称算法类型<br>
 *
 * @author Looly
 */
public enum AsymmetricAlgorithm {
    RSA("RSA"), DSA("DSA");

    private String value;

    AsymmetricAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
