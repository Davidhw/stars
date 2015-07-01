package edu.ncf.cs.David_Weinstein.csvReader;

import java.util.List;

/**
 * Interphase of class for reading CSV files.
 * 
 * @author david weinstein
 *
 */

public interface CSVReaderInterphase {
  /**
   * Does the reader's list of fields contain some list of fields?
   *
   * @param fields
   *          you want to check for.
   * @return are the fields present in reader's list of fields.
   */
  Boolean containsFields(List<String> fields);

  /**
   * Is there a next line. Ifso mov reader to it and return true. else, false.
   *
   * @return Whether or not there is a next record to load.
   */
  Boolean loadNextRecord();

  /**
   * Returns the value of the current line for a given field.
   *
   * @param field
   *          Which field you want the data of.
   * @return the current record's data for that field.
   */
  String get(String field);

  /**
   * Close the file that was being read.
   */
  void close();
}
