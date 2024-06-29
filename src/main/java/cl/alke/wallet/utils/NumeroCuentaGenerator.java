package cl.alke.wallet.utils;

import java.util.Random;

public class NumeroCuentaGenerator {

    private static final Random RANDOM = new Random();

    public static String generarNumeroCuenta() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append('-'); // Agregar un guion después de cada cuatro dígitos
            }
            sb.append(RANDOM.nextInt(10)); // Generar un dígito aleatorio entre 0 y 9
        }
        return sb.toString();
    }
}
