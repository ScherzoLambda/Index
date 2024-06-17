package server_api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import constantes.Constants;

public class ServerAPI {
    static String webService = Constants.SERVER_ENDPOINT;
    static String loginEndPoint = Constants.LOGIN_ENDPOINT;
    static int codigoSucesso = 200;
    static int codigoErroLogin = 611;
    static Map<String, String> headers = new HashMap<>();

    public static String makeRequest(String body) throws Exception {
        Log.d("MakeRequest", "onCreate() called");
        String urlParaChamada = webService;
//        headers.put("Content-Type", "application/json");
//        headers.put("api-key", "GkYXzrSsaEgankl5jLkJy2eA7eO5emqfGAb9xVwVvFQVzF1Szv1c6AkSDB39SOAQ");
        try {
            URL url = new URL(urlParaChamada);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            // Configurações da conexão
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json");
            conexao.setRequestProperty("Accept", "*/*");


            // Envia o corpo da requisição, se houver
            if (body != null && !body.isEmpty()) {
                conexao.setDoOutput(true);
                OutputStream os = conexao.getOutputStream();
                os.write(body.getBytes());
                os.flush();
            }

            if (conexao.getResponseCode() != codigoSucesso){
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());}

            BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
            StringBuilder respostaBuilder = new StringBuilder();
            String linha;
            while ((linha = resposta.readLine()) != null) {
                respostaBuilder.append(linha);
            }
            resposta.close();

            return respostaBuilder.toString();
        } catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }
    public static String makeRequestLogin(String body) throws Exception {
        Log.d("MakeRequest", "Inicio requisição login");
        String urlParaChamada = loginEndPoint;

        try {
            URL url = new URL(urlParaChamada);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            // Configurações da conexão
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json");
            conexao.setRequestProperty("Accept", "*/*");


            // Envia o corpo da requisição, se houver
            if (body != null && !body.isEmpty()) {
                conexao.setDoOutput(true);
                OutputStream os = conexao.getOutputStream();
                os.write(body.getBytes());
                os.flush();
            }

            if (conexao.getResponseCode() != codigoSucesso){
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());}

            BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
            StringBuilder respostaBuilder = new StringBuilder();
            String linha;
            while ((linha = resposta.readLine()) != null) {
                respostaBuilder.append(linha);
            }
            resposta.close();

            return respostaBuilder.toString();
        } catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }
}
