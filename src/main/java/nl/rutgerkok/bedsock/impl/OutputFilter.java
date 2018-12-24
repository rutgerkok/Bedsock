package nl.rutgerkok.bedsock.impl;

interface OutputFilter {

    /**
     * Parses a line of text.
     * 
     * @param line
     * @return True if more lines should be passed to this filter, false otherwise.
     */
    boolean parse(String line);

}
