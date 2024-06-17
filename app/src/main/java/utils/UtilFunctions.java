package utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;

public class UtilFunctions {

    /**
     * * A função abaixo gera o hash SHA256 de uma entrada em string
     * */
    public static String gerarSHA256(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02X", b);
            }
            return formatter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
