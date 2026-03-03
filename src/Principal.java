import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

        String apikey = "TU_API_KEY_AQUI";  // <-- INGRESA LA CLAVE O API-KEY DE ExchangeRate-API AQUÍ
        String moneda_origen = "";
        String moneda_objetivo = "";
        int valor_a_convertir = 1;
        String opcion = "";
        int opcion_numerica = 0;

        while (!(opcion.equalsIgnoreCase("9"))) {
            System.out.println("""
                    ***********************************************************
                    Bienvenido/a al conversor de moneda de Daniel.
                    Seleccione una de las siguientes opciones para continuar:
                    
                    1) Dólar -->> Peso colombiano
                    2) Peso colombiano -->> Dólar
                    3) Dólar -->> Peso argentino
                    4) Peso argentino -->> Dólar
                    5) Dólar -->> Real brasileño
                    6) Real brasileño -->> Dólar
                    7) Dólar -->> Peso chileno
                    8) Peso chileno -->> Dólar
                    9) Salir
                    ***********************************************************
                    """);

            Scanner entrada = new Scanner(System.in);
            System.out.println("Opción: ");
            opcion = entrada.next();

            //ESTE TRY-CATCH ES PARA EVITAR UN ERROR AL INGRESAR LETRAS
            try {
                opcion_numerica = Integer.parseInt(opcion);

                switch (opcion_numerica) {
                    case (1):
                        moneda_origen = "USD";
                        moneda_objetivo = "COP";
                        break;
                    case (2):
                        moneda_origen = "COP";
                        moneda_objetivo = "USD";
                        break;
                    case (3):
                        moneda_origen = "USD";
                        moneda_objetivo = "ARS";
                        break;
                    case (4):
                        moneda_origen = "ARS";
                        moneda_objetivo = "USD";
                        break;
                    case (5):
                        moneda_origen = "USD";
                        moneda_objetivo = "BRL";
                        break;
                    case (6):
                        moneda_origen = "BRL";
                        moneda_objetivo = "USD";
                        break;
                    case (7):
                        moneda_origen = "USD";
                        moneda_objetivo = "CLP";
                        break;
                    case (8):
                        moneda_origen = "CLP";
                        moneda_objetivo = "USD";
                        break;
                    case (9):
                        System.out.println("Saliendo de la aplicacion");
                        break;
                    default:
                        System.out.println("Ingrese una opción válida\n");
                }

                if (opcion_numerica >= 1 && opcion_numerica <= 8) {
                    System.out.println("La opción seleccionada es para convertir de " + moneda_origen + " a " + moneda_objetivo);

                    System.out.println("\nDigite la cantidad de " + moneda_origen + " que desea convertir a " + moneda_objetivo + ":");
                    valor_a_convertir = entrada.nextInt();

                    String url_str = "https://v6.exchangerate-api.com/v6/" + apikey + "/pair/" +
                            moneda_origen + "/" + moneda_objetivo + "/" + valor_a_convertir;

                    // Making Request
                    URL url = null;
                    try {
                        url = new URL(url_str);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    HttpURLConnection request;
                    try {
                        request = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        request.connect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Convert to JSON
                    JsonParser jp = new JsonParser();
                    JsonElement root = null;
                    try {
                        root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JsonObject jsonobj = root.getAsJsonObject();

                    // Accessing object
                    String req_result = jsonobj.get("conversion_result").getAsString();

                    System.out.println("\nEl valor de cambio es: " + moneda_objetivo + " " + req_result + "\n");
                }
            } catch (NumberFormatException error) {
                System.out.println("Ingrese una opción válida\n");
            }
        }
    }
}
