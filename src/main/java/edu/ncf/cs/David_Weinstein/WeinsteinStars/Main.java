package edu.ncf.cs.David_Weinstein.WeinsteinStars;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ncf.cs.David_Weinstein.csvReader.CSVReader;
import edu.ncf.cs.David_Weinstein.kdTree.KDNode;
import edu.ncf.cs.David_Weinstein.kdTree.KDTree;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

/**
 * Open the data file, read it in, and start taking in commands.
 *
 * @author d
 *
 */
public class Main {

  private static final List<String> EXPECTEDFIELDS = Arrays
      .asList(new String[] { "StarID", "ProperName", "X", "Y", "Z" });

  /**
   * Read in the datafile, and start taking in commands.
   *
   * @param args
   *          Should contain 1 arg, the location of the datafile.
   */
  public static void main(final String[] args) {
    boolean launchGui = false;
    if (args.length != 1 && args.length != 2) {
      throw new IllegalArgumentException(
          "One argument, the filepath of stars datafile, is required. --gui for a gui is optional");
    } else {
      final String dataPath = args[0];

      if (args.length == 2 && args[1].trim().equals("--gui")) {
        launchGui = true;
      }

      final CSVReader stars = new CSVReader(dataPath, ",");
      if (!stars.containsFields(EXPECTEDFIELDS)) {
        throw new IllegalArgumentException(
            "The provided dataFile does not have the expected fieldnames.");
      } else {
        final List<KDNode> kdNodes = new ArrayList<KDNode>();
        final HashMap<String, Star> properlyNamedStars = new HashMap<String, Star>();
        while (stars.loadNextRecord()) {
          final Integer starID = Integer.valueOf(stars.get("StarID"));
          final String properName = stars.get("ProperName");
          final Double starX = Double.valueOf(stars.get("X"));
          final Double starY = Double.valueOf(stars.get("Y"));
          final Double starZ = Double.valueOf(stars.get("Z"));
          final List<Double> location = Arrays.asList(new Double[] { starX,
              starY, starZ });
          final Star star = new Star(starID, properName, starX, starY, starZ);
          kdNodes.add(new KDNode(location, star));
          if (properName.length() > 1) {
            properlyNamedStars.put(properName, star);
          }

        }
        stars.close();

        final KDTree tree = new KDTree(kdNodes, 3);
        final KDNode root = tree.getRoot();
        final CommandHandler ch = new CommandHandler(tree, properlyNamedStars);
        if (launchGui) {
          class QueryHandler implements TemplateViewRoute {
            @Override
            public ModelAndView handle(final Request request, final Response res) {
              final QueryParamsMap qm = request.queryMap();
              final String query = qm.value("query");
              final String queryResult = ch.runCommand(query);
              final Map<String, Object> variables = ImmutableMap.of(
                  "queryResult", queryResult);
              return new ModelAndView(variables, "query.ftl");
            }
          }
          Spark.externalStaticFileLocation("src/main/resources/static");

          // Development is easier if we show exceptions in the browser.
          Spark.exception(Exception.class, new ExceptionPrinter());

          // We render our responses with the FreeMaker template system.
          final FreeMarkerEngine freeMarker = createEngine();

          Spark.get("/gui", new GuiHandler(), freeMarker);
          Spark.post("/query", new QueryHandler(), freeMarker);
        } else {
          ch.inputLoop();
        }

      }
    }

  }

  /**
   * server error constant
   */
  private static final int INTERNAL_SERVER_ERROR = 500;

  /**
   * Prints server errors.
   */
  private static class ExceptionPrinter implements ExceptionHandler {

    /**
     * Prints server errors.
     */
    @Override
    public void handle(final Exception exception, final Request req,
        final Response res) {
      res.status(INTERNAL_SERVER_ERROR);
      final StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        exception.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Gives the location of templates and returns a freemarker engine.
   *
   * @return a FreeMarker engine
   */
  private static FreeMarkerEngine createEngine() {
    final Configuration config = new Configuration();
    final File templates = new File(
        "src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (final IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Starts a new game by returning a view of a title and board.
   */
  private static class GuiHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      final Map<String, Object> variables = ImmutableMap.of("title",
          "Stars Query System");
      return new ModelAndView(variables, "gui.ftl");
    }
  }

  /**
   * Moves to the given position and returns the resulting board.
   */

}
