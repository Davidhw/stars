package edu.ncf.cs.David_Weinstein.csvReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Reads CSV files.
 *
 * @author david weinstein.
 *
 */
public class CSVReader implements CSVReaderInterphase {
  /**
   * Reads through the data file.
   */
  private BufferedReader br;
  /**
   * How the datafile values are separated.
   */
  private final String delimiter;
  /**
   * Collumns data is stored under.
   */
  private List<String> fields;
  /**
   * The current line of the datafile being read.
   */
  private String currentLine;
  /**
   * The last values read in from the datafile.
   */
  private List<String> currentValues;

  /**
   * Class for reading in CSV data.
   *
   * @param filePath
   *          of CSV file.
   * @param inputDelimiter
   *          used to separate data.
   */
  public CSVReader(final String filePath, final String inputDelimiter) {
    delimiter = inputDelimiter;

    try {
      br = new BufferedReader(new FileReader(filePath));
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      fields = Arrays.asList(br.readLine().split(delimiter));
    } catch (final IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Is there a next line. Ifso mov reader to it and return true. else, false.
   *
   * @return Whether or not there is a next record to load.
   */
  @Override
  public final Boolean loadNextRecord() {
    try {
      currentLine = br.readLine();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    if (currentLine == null) {
      return false;
    } else {
      currentValues = Arrays.asList(currentLine.split(delimiter));
      if (currentValues.size() != fields.size()) {
        System.out.println("In this row, the number of values specified is "
            + "different from the number of fields. Check your CSV file's "
            + "headers and check that there is a value for "
            + "each one in each row.");
        throw new IllegalArgumentException();
      } else {
        return true;
      }
    }
  }

  /**
   * Close the file that was being read.
   */
  @Override
  public final void close() {
    try {
      br.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the value of the current line for a given field.
   *
   * @param field
   *          Which field you want the data of.
   * @return the current record's data for that field.
   */
  @Override
  public final String get(final String field) {
    if (!fields.contains(field)) {
      throw new IllegalArgumentException();
    } else {
      // this could be done more efficiently by keeping current values in
      // a hash
      return currentValues.get(fields.indexOf(field));
    }
  }

  /**
   * Does the reader's list of fields contain some list of fields?
   *
   * @param someFields
   *          you want to check for.
   * @return are the fields present in reader's list of fields.
   */
  @Override
  public final Boolean containsFields(final List<String> someFields) {
    return fields.containsAll(someFields);
  }
}
