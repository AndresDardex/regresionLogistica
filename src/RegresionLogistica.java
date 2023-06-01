import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class RegresionLogistica {

    public static void main(String[] args) {
        // Generar datos aleatorios
        Random random = new Random();
        int dataSize = 100; // Tamaño de los datos
        double[][] x = new double[dataSize][2];
        int[] y = new int[dataSize];
        for (int i = 0; i < dataSize; i++) {
            x[i][0] = 1; // Agregar el término de intercepción
            x[i][1] = random.nextDouble() * 10; // Generar número aleatorio entre 0 y 10 para x
            y[i] = (random.nextDouble() < 1 / (1 + Math.exp(-2 * x[i][1]))) ? 1 : 0; // Generar número aleatorio para y con una distribución logística
        }

        // Estimación inicial de los parámetros
        double[] coefficients = {0, 0};

        // Ajustar el modelo de regresión logística usando el algoritmo de descenso de gradiente
        int maxIterations = 10000;
        double learningRate = 0.01;
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            double[] gradient = new double[2];
            for (int i = 0; i < dataSize; i++) {
                double predicted = 1 / (1 + Math.exp(-coefficients[0] * x[i][0] - coefficients[1] * x[i][1]));
                gradient[0] += (predicted - y[i]) * x[i][0];
                gradient[1] += (predicted - y[i]) * x[i][1];
            }
            coefficients[0] -= learningRate * gradient[0] / dataSize;
            coefficients[1] -= learningRate * gradient[1] / dataSize;
        }

        double intercept = coefficients[0];
        double slope = coefficients[1];
        System.out.println("Intercept: " + intercept);
        System.out.println("Slope: " + slope);

        // Crear la serie de datos para los puntos
        XYSeries series = new XYSeries("Data");
        for (int i = 0; i < dataSize; i++) {
            series.add(x[i][1], y[i]);
        }

        // Crear la serie de datos para la curva de regresión logística
        XYSeries logisticCurve = new XYSeries("Logistic Curve");
        for (double xi = 0; xi <= 10; xi += 0.1) {
            logisticCurve.add(xi, 1 / (1 + Math.exp(-intercept - slope * xi)));
        }

        // Crear la colección de series de datos
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(logisticCurve);

        // Crear el gráfico de dispersión con curva de regresión logística
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Logistic Regression", // Título del gráfico
                "X", // Etiqueta del eje X
                "Y", // Etiqueta del eje Y
                dataset, // Datos
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Obtener el objeto Plot y personalizarlo
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRangeAxis().setRange(0, 1); // Establecer el rango del eje Y entre 0 y 1
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false); // Ocultar líneas entre los puntos de datos
        renderer.setSeriesLinesVisible(1, true); // Mostrar curva de regresión logística
        renderer.setSeriesShapesVisible(1, false); // Ocultar puntos en la curva de regresión logística
        plot.setRenderer(renderer);

        // Mostrar el gráfico en una ventana
        ChartFrame frame = new ChartFrame("Logistic Regression", chart);
        frame.pack();
        frame.setVisible(true);
    }
}