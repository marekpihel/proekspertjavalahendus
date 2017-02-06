package proekspert;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.ApplicationFrame;


import java.awt.*;

/**
 * Created by mpihe on 2/5/2017.
 */
public class Chart extends ApplicationFrame {
    public Chart(String title) {
        super(title);
    }

    public void generateChart(CategoryDataset categoryDataset) {
        JFreeChart chart = ChartFactory.createBarChart("Hourly request statistics",
                "Hours",
                "Requests",
                categoryDataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);


        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setDrawBarOutline(false);

        GradientPaint paint = new GradientPaint(0f, 0f, Color.black, 0f, 0f, Color.black);

        barRenderer.setSeriesPaint(0, paint);

        CategoryAxis domainaxis = plot.getDomainAxis();
        domainaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

    }
}
