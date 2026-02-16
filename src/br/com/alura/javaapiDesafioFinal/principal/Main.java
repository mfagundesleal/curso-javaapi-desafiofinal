package br.com.alura.javaapiDesafioFinal.principal;

import br.com.alura.javaapiDesafioFinal.dto.CepDTO;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static String requestAPI(String url) {
        String endereco = url.replaceAll("\s", "-");
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return res.body();
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public static void main(String[] args) {
//        https://viacep.com.br/ws/01001000/json/
        try{
//            inicializa
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
            FileWriter salvarArquivo = new FileWriter("cep.json");
            Scanner ler = new Scanner(System.in);

//            menu
            System.out.println("""
                
                ****** CONSULTA CEP ******
                """);
            System.out.println("Digite seu CEP, por gentileza, somente números: ");
            String cep = ler.nextLine();
//            trata o CEP (remove espaço em branco, pontos e hífens)
            String cepSemCaracteres = cep.replaceAll("[\\s\\.\\-]", "");
//            consome função de request
            String resultado = requestAPI("https://viacep.com.br/ws/"+cepSemCaracteres+"/json/");
//            utiliza o DTO para formatar o JSON (não precisa salvar em um objeto pois será utiliza na criação do arquivo) e depois salva o arquivo.
            CepDTO cepDTO = gson.fromJson(resultado, CepDTO.class);
            String resultadoJson = gson.toJson(cepDTO);
            salvarArquivo.write(resultadoJson);
            salvarArquivo.close();
            System.out.println("Arquivo 'cep.json' salvo com sucesso! Confira. :)");
        }catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
        }


    }
}
