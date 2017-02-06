package proekspert;


import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 * Created by mpihe on 2/3/2017.
 */
public class Controller {

    public static void main(String[] args) {
        long start = initStart();
        boolean hasNumber;
        ArrayList<Resource> resources = new ArrayList<>();
        HashMap<String, Double> resourcesAverage = new HashMap<>();
        ArrayList<Query> querys = new ArrayList<>();


        if (Arrays.stream(args).filter(argument -> argument.equals("-h")).count() > 0) {
            displayHelp();
        } else if (args.length == 2 && args[0].split("\\.").length == 2) {
            try {
                Integer.parseInt(args[1]);
                hasNumber = true;
            } catch (NumberFormatException e) {
                hasNumber = false;
            }
            if (hasNumber) {
                try (BufferedReader fileReader = new BufferedReader(new FileReader(args[0]))) {
                    String fileLine;
                    while ((fileLine = fileReader.readLine()) != null) {
                        if (fileLine.split(" ").length == 8 || fileLine.split(" ").length == 9) {
                            resources.add(new Resource(fileLine));
                        } else if (fileLine.split(" ").length == 7) {
                            querys.add(new Query(fileLine));
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Given file is not in the same folder as the program or does not exist!");
                } catch (IOException e) {
                    System.out.println("Program encountered error");
                }

                for (Resource resource : resources) {
                    if (!resourcesAverage.containsKey(resource.getResourceName())) {
                        resourcesAverage.put(resource.getResourceName(), resources.stream().filter(resource1 -> resource1.getResourceName().equals(resource.getResourceName())).collect(Collectors.toList()).stream().mapToLong(Resource::getRequestDuration).average().getAsDouble());
                    }
                }
                for (Query query : querys) {
                    if (query.getIsResource()) {
                        if (!resourcesAverage.containsKey(query.getUriQuery())) {
                            resourcesAverage.put(query.getUriQuery(), querys.stream().filter(query1 -> query1.getUriQuery().equals(query.getUriQuery())).collect(Collectors.toList()).stream().mapToLong(Query::getRequestDuration).average().getAsDouble());
                        }
                    }
                }

                ArrayList<String> resourceNamesArray = new ArrayList<>();
                ArrayList<Double> resourceAveragesArray = new ArrayList<>();

                for (String key : resourcesAverage.keySet()) {
                    if (resourceAveragesArray.isEmpty()) {
                        resourceNamesArray.add(0, key);
                        resourceAveragesArray.add(0, resourcesAverage.get(key));
                        continue;
                    }
                    for (int index = 0; index < resourceAveragesArray.size(); index++) {
                        if (resourceAveragesArray.get(index) < resourcesAverage.get(key)) {
                            resourceNamesArray.add(index, key);
                            resourceAveragesArray.add(index, resourcesAverage.get(key));
                            break;
                        } else if (index == resourceAveragesArray.size() - 1) {
                            if (resourceAveragesArray.get(index) > resourcesAverage.get(key)) {
                                resourceNamesArray.add(index + 1, key);
                                resourceAveragesArray.add(index + 1, resourcesAverage.get(key));
                                break;
                            }
                        }
                    }


                }

                // Print n resource request averages to command line

                int loopCycles;
                if (resourceAveragesArray.size() >= Integer.parseInt(args[1])) {
                    loopCycles = Integer.parseInt(args[1]);
                } else {
                    loopCycles = resourceAveragesArray.size();
                }
                for (int index = 0; index < loopCycles; index++) {
                    System.out.println(resourceNamesArray.get(index) + ": " + resourceAveragesArray.get(index));
                }


                // Hourly stats

                HashMap<Integer, Integer> hourlyStatistics = new HashMap<>();

                for (int hour = 0; hour < 24; hour++) {
                    hourlyStatistics.put(hour, 0);
                }


                for (Resource resource : resources) {
                    int hour = Integer.parseInt(resource.getTimeStamp().split(":")[0]);
                    if (hourlyStatistics.keySet().contains(hour)) {
                        hourlyStatistics.put(hour, hourlyStatistics.get(hour) + 1);
                    }

                }
                for (Query query : querys) {
                    int hour = Integer.parseInt(query.getTimeStamp().split(":")[0]);
                    if (hourlyStatistics.keySet().contains(hour)) {
                        hourlyStatistics.put(hour, hourlyStatistics.get(hour) + 1);
                    }
                }


                // Stuff for Chart


                DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
                for (Integer key : hourlyStatistics.keySet()) {
                    defaultCategoryDataset.addValue((float) hourlyStatistics.get(key), "1", Integer.toString(key));
                }
                CategoryDataset categoryDataset = defaultCategoryDataset;

                Chart chart = new Chart("Hourly request statistics");
                chart.generateChart(categoryDataset);

                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);


            }

        } else {
            System.out.println("Invalid number of arguments or invalid arquements");
        }


        printProgramRunTime(start);
    }


    private static long initStart() {
        long start = 0;
        start = System.currentTimeMillis();
        return start;
    }

    private static void displayHelp() {
        System.out.println("When running the program enter 2 arguments log filename and\n" +
                "number of resources you would like to get the overview of. \n" +
                "The log file needs to be in the same folder as the jar file.\n" +
                "Example: java -jar proekspert.jar example.log 10\n\n" +
                "To display help run the program with argument -h.\n" +
                "Example: java -jar proekspert.jar -h");
    }

    private static void printProgramRunTime(long start) {
        long programRunTimeLong = System.currentTimeMillis() - start;

        String programRunTime = "";
        programRunTime += "The program took: ";
        if (programRunTimeLong > 100) {
            programRunTime += programRunTimeLong / 100.0f + " s ";
        } else {
            programRunTime += programRunTimeLong + " ms ";
        }
        programRunTime += "to finish.";

        System.out.println(programRunTime);
    }
}
