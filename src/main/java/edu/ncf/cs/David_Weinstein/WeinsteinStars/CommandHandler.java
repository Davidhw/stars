package edu.ncf.cs.David_Weinstein.WeinsteinStars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ncf.cs.David_Weinstein.kdTree.KDNode;
import edu.ncf.cs.David_Weinstein.kdTree.KDTree;
/** 
 * Runs queries on a given KDTree of stars.
 * @author david weinstein.
 */
public class CommandHandler {
  KDTree tree;
  HashMap<String, Star> properlyNamedStars;

  /**
   * Creates a command handler.
   * 
   * @param inputTree
   *          KDTree
   * @param inputProperlyNamedStars
   *          Matches names of stars to their locations.
   */
  public CommandHandler(final KDTree inputTree,
      final HashMap<String, Star> inputProperlyNamedStars) {
    tree = inputTree;
    properlyNamedStars = inputProperlyNamedStars;
  }

  /**
   * Regex representing a decimal number.
   */
  private final String DECIMAL_NUMBER = "((?:\\-?)\\d+(?:\\.\\d+)?)";
  /**
   * Regex representing a three space delimited decimal numbers.
   */
  private final String THREE_DECIMAL_NUMBERS = DECIMAL_NUMBER + " "
      + DECIMAL_NUMBER + " " + DECIMAL_NUMBER;
  /**
   * Regex representing 1 or more numbers.
   */
  private final String DIGITS = "(\\d+)";
  /**
   * regex representing a word surrounded by " and ".
   */
  private final String WORD_IN_QUOTES = "\\\"(\\w+)\\\"";

  /**
   * matches 'neighbors' anInt double double double.
   */
  private final String NEIGHBORS_LOC_STRING = "neighbors " + DIGITS + " "
      + THREE_DECIMAL_NUMBERS;

  /**
   * matches neighbors int aStringName.
   */
  private final String NEIGHBORS_NAME_STRING = "neighbors " + DIGITS + " "
      + WORD_IN_QUOTES;
  /**
   * regex matching radius r x y z.
   */
  private final String RADIUS_LOC_STRING = "radius " + DECIMAL_NUMBER + " "
      + THREE_DECIMAL_NUMBERS;
  /**
   * regex matching adius r “name".
   */
  private final String RADIUS_NAME_STRING = "radius " + DECIMAL_NUMBER + " "
      + WORD_IN_QUOTES;

  /**
   * pattern for matching neigbors n x y z.
   */
  private final Pattern neighborsLocPattern = Pattern
      .compile(NEIGHBORS_LOC_STRING);
  /**
   * pattern for matching neigbors n name.
   */
  private final Pattern neighborsNamePattern = Pattern
      .compile(NEIGHBORS_NAME_STRING);
  /**
   * pattern for matching radius n x y z.
   */
  private final Pattern radiusNamePattern = Pattern.compile(RADIUS_NAME_STRING);
  /**
   * pattern for matching radius n name.
   */
  private final Pattern radiusLocPattern = Pattern.compile(RADIUS_LOC_STRING);

  /**
   * takes three doubles and returns a list of them.
   *
   * @param x
   *          is double 1.
   * @param y
   *          is double 2.
   * @param z
   *          is double 3.
   * @return list of the three doubles 1,2,3.
   */
  private List<Double> xyzToList(final double x, final double y, final double z) {
    return Arrays.asList(new Double[] { x, y, z });
  }

  /**
   * takes a list of nodes and returns a list of their ids.
   *
   * @param nodes
   *          to get ids of.
   * @return a list of node ids.
   */
  private List<String> idsOfNodes(final List<KDNode> nodes) {
    final List<String> ids = new ArrayList<String>();
    for (final KDNode node : nodes) {
      ids.add(String.valueOf(((Star) node.getThingStored()).getStarID()));
    }
    return ids;
  }

  /**
   * Reads takes in and executes queries from STDin.
   */
  public void inputLoop() {
    final int controlD = 4;
    BufferedReader input = null;
    try {
      input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    } catch (final UnsupportedEncodingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    StringBuilder command = new StringBuilder();
    while (true) {
      try {
        final char inputChar = (char) input.read();
        if (inputChar == '\n') {
          System.out.println(command.toString());
          System.out.println(runCommand(command.toString()));
          command = new StringBuilder();
        } else {
          if (inputChar != controlD) {
            command.append(inputChar);
          } else {
            break;
          }
        }
      } catch (final IOException e) {
        System.out.println("An error occured while reading in your input.");
      }
    }

  }

  /**
   * Gives the location of a star from the given proper name.
   *
   * @param name
   *          of the star
   * @return list of x,y,z of star's location
   */
  private List<Double> starToLocation(final String name) {
    if (properlyNamedStars.containsKey(name)) {
      final Star star = properlyNamedStars.get(name);
      return xyzToList(star.getX(), star.getY(), star.getZ());
    } else {
      return null;
    }
  }

  /**
   * Calls radius search on a location and radius.
   *
   * @param location
   *          to search around.
   * @param radius
   *          to search within.
   * @return ids of stars in radius of the location.
   */
  private List<String> callRadiusSearch(final List<Double> location,
      final double radius) {
    final List<String> output = new ArrayList<String>();
    if (radius < 0) {
      output.add("Please input a non-negative number for the search radius.");
    } else {
      output.addAll(idsOfNodes(tree.neighborsInRange(location, radius)));
    }
    return output;
  }

  /**
   * Calls nearest neighbors search on a location and n (# neighbors).
   *
   * @param location
   *          to search around.
   * @param numNeighbors
   *          number of neighbors to return.
   * @return neighbors of location.
   */
  private List<String> callNN(final List<Double> location,
      final int numNeighbors) {
    final List<String> output = new ArrayList<String>();
    if (numNeighbors < 0) {
      output.add("Please input a non-negative number of neighors");
    } else {
      output.addAll(idsOfNodes(tree.nearestNeighbors(location, numNeighbors)));
    }
    return output;
  }

  /**
   * Either joins a list of strings by "\n" or returns a "0" if empty list.
   *
   * @param input
   *          list of strings
   * @return "0" or list joined by newline
   */
  private String joinOutput(final List<String> input) {
    if (input.size() == 0) {
      return "0";
    } else {
      return String.join("\n", input);
    }
  }

  /**
   * determines if the input is a query and if so, returns the query's output.
   *
   * @param input
   *          query.
   * @return query output or error message.
   */
  public String runCommand(final String input) {
    double x, y, z;
    List<Double> location;

    System.out.println("input");
    System.out.println(input);
    final Matcher neighborsLocMatcher = neighborsLocPattern.matcher(input);
    final Matcher neighborsNameMatcher = neighborsNamePattern.matcher(input);
    final Matcher radiusNameMatcher = radiusNamePattern.matcher(input);
    final Matcher radiusLocMatcher = radiusLocPattern.matcher(input);

    input.trim();
    if (neighborsLocMatcher.matches()) {

      final int numNeighbors = Integer.valueOf(neighborsLocMatcher.group(1));
      x = Double.valueOf(neighborsLocMatcher.group(2));
      y = Double.valueOf(neighborsLocMatcher.group(3));
      z = Double.valueOf(neighborsLocMatcher.group(4));
      location = xyzToList(x, y, z);
      return joinOutput(callNN(location, numNeighbors));
    } else {
      if (neighborsNameMatcher.matches()) {
        final int numNeighbors = Integer.valueOf(neighborsNameMatcher.group(1)) + 1;
        final String starName = neighborsNameMatcher.group(2);
        location = starToLocation(starName);
        if (location == null) {
          System.out.println("Sorry, no star with that name was found.");
        } else {
          final List<String> output = callNN(location, numNeighbors);
          if (output.size() > 0) {
            output.remove(0);
          }
          return joinOutput(output);
        }

      } else {
        if (radiusLocMatcher.matches()) {
          final double radius = Double.valueOf(radiusLocMatcher.group(1));
          x = Double.valueOf(radiusLocMatcher.group(2));
          y = Double.valueOf(radiusLocMatcher.group(3));
          z = Double.valueOf(radiusLocMatcher.group(4));
          location = xyzToList(x, y, z);
          return joinOutput(callRadiusSearch(location, radius));

        } else {
          if (radiusNameMatcher.matches()) {
            final double radius = Double.valueOf(radiusNameMatcher.group(1));
            final String starName = radiusNameMatcher.group(2);
            location = starToLocation(starName);
            return joinOutput(callRadiusSearch(location, radius));
          } else {
            return "Your input did not match any known commands, please try again.\n";
          }
        }
      }

      // should be unreachable
      return "That command was truely not recognized";
    }

  }

}
