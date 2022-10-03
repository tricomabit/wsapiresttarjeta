package com.apitarjeta.wsapiresttarjeta;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import com.apitarjeta.wsapiresttarjeta.SearchBin.BigBinarySearcher;
import com.apitarjeta.wsapiresttarjeta.SearchBin.BinarySearcher;

@RestController
@RequestMapping("/Tarjeta")
public class TarjetaController {
    
    //#region Get
    // @GetMapping
    // public String Tarjeta()
    // {
    //     return "ok";
    // }
    //#endregion

    @PostMapping("/ConsultaTarjeta")
    @ResponseBody
    public Tarjeta ConsultaTarjeta (@RequestBody Tarjeta tarjeta) throws URISyntaxException, IOException
    {
        String home = System.getProperty("user.home");
        File f = new File(home + File.separator + "Desktop" + File.separator + "binaryTest.bin");

        URL res = getClass().getClassLoader().getResource("OperacionesBinariasTest.bin");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        try (
            InputStream inputStream = new FileInputStream(file);
        ) {

            byte[] array = inputStream.readAllBytes();

            
            try {

                
                BinarySearcher bs = new BinarySearcher();

                //convierte Numero Tarjeta en bytes[]
                byte[] searchBytes = tarjeta.NumTarjeta.getBytes("utf-8");
        
                int index = bs.indexOf(array, searchBytes);
        
                System.out.println("index=" + index);

                if (index != -1) {
                    byte[] out = null;
                    // int outcount = 0;
                    // long valor = valor7bytesToInt(array);
                    //  for (int i=index+7;i<index+9;i++) {
                    //     //do something with mFFTBytes[i]
                    //     System.out.print(array[i] +" ");
                    //     byte a = array[i];

                        ByteArrayOutputStream output = new ByteArrayOutputStream();

                        output.write(array[index+7]);
                        output.write(array[index+8]);

                        out = output.toByteArray();
                        // outcount++;

                        // String bytetext = new String(array[i],"UTF-8");
                        

                    // }

                    tarjeta.CodTarjeta = new String(out, "UTF-8");
                    System.out.println("Numero de operacion Tarjeta : " + tarjeta.CodTarjeta.toString());
                    
                }


                //#region Implementaciones de prueba
                // UTF-8 without BOM
                //No encuentra
                // BigBinarySearcher bbs = new BigBinarySearcher();
                // byte[] iamBigSrcBytes = "Hello world.It's a small world.".getBytes("utf-8");
        
                // byte[] searchBytes = "734629483".getBytes("utf-8");
        
                // List<Integer> indexList = bbs.searchBigBytes(array, searchBytes);
        
                // System.out.println("indexList=" + indexList);


                // funcion que Itera bytes
                // char[] cs = new char[array.length];
                // for (int i = 0; i < cs.length; ++i)
                // {
                //   cs[i] = (char) array[i];
                // }

                // for (int i=0;i<array.length;i++) {
                //     //do something with mFFTBytes[i]
                //     System.out.print(array[i] +" ");
                //     // String bytetext = new String(array[i],"UTF-8");

                // }

                // String text = new String(array, "UTF-8");
                // System.out.println(text);

                // // String specific = "734629483";
                // if (text.indexOf(tarjeta.NumTarjeta) >= 0) {
                //     System.out.println("Found " + tarjeta.NumTarjeta);
                // }else{
                //     System.out.println("Not found " + tarjeta.NumTarjeta);
                // } 


                // valor7bytesToInt(array);
                //#endregion
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // String strHexa = byteArrayToHexStringSinEspacios(array);
          
 
        } catch (FileNotFoundException e) {
                e.getMessage().toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tarjeta;
    }


        /** Convierte un arreglo de bytes en un String con su correspondiente representacion hexadecimal.
     * @param bytes arreglo de bytes que se desea representar en hexadecimal.
     * @return String con la representacion hexadecimal, cada par de caracteres se retorna separado por espacios. */
    public static String byteArrayToHexStringSinEspacios(final byte[] bytes) {
        final int desplazamiento = 4;
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            // int v = bytes[j] & MASK_FF;
            int v = bytes[j];
            hexChars[j * 2] = hexArray[v >>> desplazamiento];
            final int mascara = 0x0F;
            hexChars[j * 2 + 1] = hexArray[v & mascara];
        }
        return new String(hexChars);
    }


    /** Metodo que obtiene el numero en int de un arreglo de 2 bytes.
     * @param leido arreglo de byte leido con el numero de la tarjeta
     * @return codigo de producto
     * @throws TablasReferenciasException TablasReferenciasException */
    public static int valor2bytesToInt(final byte[] leidoIn) throws Exception{
        
        int valorreturn=0;
        try{
                // byte[] leido = Util.invertirCadena(leidoIn);
            byte[] leido = leidoIn;
            final int largoValor = 2;
            if (leido.length != largoValor) {
                // throw new TablasReferenciasException("Arreglo debe ser de 2 bytes.");
            }
            final byte largoInt = 4;
            byte[] bytes = new byte[largoInt];
            final int indiceValor = 2;
            bytes[indiceValor] = leido[0];
            bytes[indiceValor + 1] = leido[1];
            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            valorreturn = byteBuffer.getInt();
            
        }catch(Exception ex){

            throw ex;
        }
        return valorreturn;
    }

    /** Metodo que obtiene el numero en long de un arreglo de 7 bytes.
     * @param leido arreglo de byte leido con el numero de la tarjeta
     * @return numero de tarjeta
     * @throws TablasReferenciasException Arreglo debe ser de 7 bytes. */
    public final long valor7bytesToInt(final byte[] leidoIn) throws Exception {

        try {
            
            // byte[] leido = Util.invertirCadena(leidoIn);
            byte[] leido = leidoIn;
            // final int largoValor = MAXIMO_BYTE_TARJETA;
            final int largoValor = 7;
            if (leido.length != largoValor) {
                // throw new TablasReferenciasException("Arreglo debe ser de 7 bytes.");
            }
            final byte largoLong = 8;
            byte[] bytes = new byte[largoLong];
            final int indiceValor = 1;
            for (int i = 0; i < largoValor; i++) {
                bytes[indiceValor + i] = leido[0 + i];
            }
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.put(bytes);
            buffer.flip(); // need flip
            return buffer.getLong();
        } catch (Exception e) {
            throw e;
        }

    }

}
