package me.wuwenbin.modules.utils;


import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.security.Encrypt;
import me.wuwenbin.modules.utils.security.asymmetric.KeyType;
import me.wuwenbin.modules.utils.security.asymmetric.RSA;
import me.wuwenbin.modules.utils.util.Maps;
import me.wuwenbin.modules.utils.web.Controllers;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * created by Wuwenbin on 2017/12/19 at 10:45
 */
public class Testx {


    public static void main(String[] args) throws NoSuchFieldException {
        Field field = Testx.class.getDeclaredField("id");

        Map<String, Object> map = Maps.hashMap("k1", 1, "k2", "ssas1", "ksss", 2);
        for (String key : map.keySet()) {
            System.out.println("key:" + key + ",value:" + map.get(key));
        }
    }

    private int addUser(String mss) throws Exception {
        if (!mss.equals("a")) {
            throw new Exception("测试错误！");
        }
        return 1;
    }

    private boolean prev(boolean f) {
        return f;
    }

    private void delete(Object o) {
        System.out.println("delete---");
    }

    @Test
    public void testRests() {
        R r0 = Controllers.builder("删除").execLight(1, this::delete);
//        R r = Controllers.builder("添加用户").exec(() -> addUser("aa") > 1);
//        System.out.println(r);
        R r1 = Controllers.builder("添加用户").exec(
                () -> true,
                () -> addUser("a") == 1,
                () -> R.ok("啦啦啦啦"),
                (r) -> r.put("another", 222),
                UnaryOperator.identity(),
                UnaryOperator.identity());
        System.out.println(r1);
    }

    @Test
    public void testEncrypt() {
//        KeyPair keyPair = SecurityUtils.generateKeyPair("RSA");
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        byte[] puk = publicKey.getEncoded();
//        byte[] prk = privateKey.getEncoded();
//        String pukey= Encrypt.base64.encode(puk);
//        String prkey = Encrypt.base64.encode(prk);
//        System.out.println(pukey);
//        System.out.println(prkey);

        String publicK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ4uh+ee6p7XnvLYjXcdWIM8xxDotolEdhpTPzWSEoqkezHn1O9unmRThYvJadKCv/nXoH3oUu64vXEb5HkFddgbzEdeleqQJWvRxxgNWBNY8/gWOZD5hlwGnu5+T4AG268lLkIhT26Jni0fG987sfjVzedEiXgdkJM5zYVXaoWwIDAQAB";
        String privateK = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIni6H557qntee8tiNdx1YgzzHEOi2iUR2GlM/NZISiqR7MefU726eZFOFi8lp0oK/+degfehS7ri9cRvkeQV12BvMR16V6pAla9HHGA1YE1jz+BY5kPmGXAae7n5PgAbbryUuQiFPbomeLR8b3zux+NXN50SJeB2QkznNhVdqhbAgMBAAECgYBrPyASb4hghyMWE1aiLBonzBIxxVxNRTeYDUSw/Bw8YuVNvmM8rfQTdLd03YpQRlSihrvJrxvr+K1V5KkZRH2ORVGBc3R/2XgBEKG5yLvFhbizlFZfgHc85OZu5DdldDGO4OcIItUNyH/YBaKib8eYcZsspvTdA1My/2az1j+WQQJBAOSzwj6OAWiROBiJPwwMWTq3J6ty0FYXl0kFSH+cYjEKTcZrlW098lG3gnLpYZ8/UmHsHkL2y34wgh6xytvspLECQQCaWCybyluQfvfZNA4X6HP6VKqeULVTJcxJO5b+/1LOcGsewYaGAdqbNxucPVL3c+pfYbFoH7S7BtKAagbkFRDLAkEAj8YyJwE/AW3JxiMB76ETt0XNOotDTdwmz1Dy8sZtNEc1/bdEiRiYbabf6z2skWBNeiHmJFLiOsJkCsfar8dlAQJABZ+44GRbOcSmm09+Q/jAYRq8rNAcC0+RRgnZ0qjTB5qpDJHYqQFgSc+UPfkuEL40iA0zPawKFdaFzRAP+DecSQJBAL/eprBDwrEqv6NB3TlJQ1CsYCG14XUUfIMmSas2OgW67bgNsa7Y3gLz5xBrYJE8Ll5lNWuz4r6wDGedXok6EeY=";

//        RSA rsa = new RSA();
//        System.out.println(rsa.getPrivateKeyBase64());
//        System.out.println(rsa.getPublicKeyBase64());
//        System.out.println(publicKey.length());
//        System.out.println(privateKey.length());
        RSA rsa = new RSA(Encrypt.base64.decode(privateK), Encrypt.base64.decode(publicK));
//        String sa= UUID.randomUUID().toString();
//        System.out.println(sa);
        String miwen = rsa.encryptStr("62f25aad-ed4c-47f6-b5c8-9861c7169833", KeyType.PublicKey);
//        String miwen = "3DF7A6E10B0D834ACB42508790F33F91D11AC1029E5BC2FDF514567157960040DE6DE5FC8A98B8C59F9F022810E73A0A526BC656144FE0EF024FCE8DB2114057F2F1EDAE4AC48709D719C5F6FA39C39C4DE5CF52E03BE460F460AE0B860C8B48100E1ED7CC0731F5B3108A3866E6C32D11990410F3A0B45EE182557117C44D31";
        System.out.println(miwen);
        String mingwen = rsa.decryptStr(miwen, KeyType.PrivateKey);
        System.out.println(mingwen);
    }
}
