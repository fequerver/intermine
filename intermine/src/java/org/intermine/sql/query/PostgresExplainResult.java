package org.intermine.sql.query;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.*;

/**
 * Subclass of ExplainResult specific to PostgreSQL.
 *
 * @author Matthew Wakeling
 * @author Andrew Varley
 * @version 1.0
 */
public class PostgresExplainResult extends ExplainResult
{
    /*
     * Fields inherited from ExplainResult:
     * protected long rows, start, complete, width, estimatedRows
     */

    /**
     * Constructs an instance of PostgresExplainResult without any data.
     *
     */
    protected PostgresExplainResult() {
    }

    /**
     * Constructs an instance of PostgresExplainResult for a given Query and
     * database Connection.
     *
     * @param query the org.intermine.sql.query.Query to be explained
     * @param database a java.sql.Connection by which to access the database
     * @throws SQLException if a database error occurs
     * @throws NullPointerException if either query or database are null
     */
    public PostgresExplainResult(Query query, Connection database) throws SQLException {
        this(query.getSQLString(), database);
    }

    /**
     * Constructs an instance of PostgresExplainResult for a given Query String and database
     * Connection.
     *
     * @param query the String query to be explained
     * @param database a java.sql.Connection by which to access the database
     * @throws SQLException if a database error occurs
     * @throws NullPointerException if either query or database are null
     */
    public PostgresExplainResult(String query, Connection database) throws SQLException {
        if ((query == null) || (database == null)) {
            throw new NullPointerException("Arguments cannot be null");
        }

        Statement s = database.createStatement();
        if (!query.toUpperCase().startsWith("EXPLAIN ")) {
            query = "explain " + query;
        }
        try {
            s.execute(query);
            retrieveExplainString(s);
            s.close();
        } catch (SQLException e) {
            SQLException e2 = new SQLException("Error running query \"" + query + "\"");
            e2.initCause(e);
            throw e2;
        }
    }

    /**
     * Constructs an instance of ExplainResult given a PreparedStatement
     * object.  Assumes that sql string already has EXPLAIN at beginning
     *
     * @param stmt the PreparedStatement to be explained
     * @throws SQLException if the query cannot be explained by that database
     */
    public PostgresExplainResult(PreparedStatement stmt) throws SQLException {
        if (stmt == null) {
            throw (new NullPointerException("PreparedStatement argument cannot be null"));
        }

        Connection database = stmt.getConnection();
        if (database == null) {
            throw (new NullPointerException("Failed to retrieve Connection"
                                            + " from PreparedStatement"));
        }

        stmt.execute();
        retrieveExplainString(stmt);
        stmt.close();
    }


    /**
     * Retrieve EXPLAIN String from post-7.3 databases
     *
     * @param stmt the Statement that ran the EXPLAIN
     * @throws SQLException if a database error occurs
     */
    protected void retrieveExplainString(Statement stmt) throws SQLException {

        ResultSet results = stmt.getResultSet();

        if ((results == null) || !results.next()) {
            throw (new SQLException("Failed to get a valid explain string from database"));
        }

        if (stmt.getMoreResults()) {
            throw new SQLException("Database returned more than ResultSet while EXPLAINing");
        }

        String text = results.getString(1);
        try {
            parseWarningString(text);
        } catch (RuntimeException e) {
            throw (new SQLException("Error parsing EXPLAIN string: " + e));
        }
    }

    /**
     * Parses the warning returned by the database into statistics for the
     * ExplainResult object.
     *
     * @param text the String returned by the database
     * @throws IllegalArgumentException if text is not a valid EXPLAIN result
     * @throws NullPointerException if text is null
     */
    void parseWarningString(String text) throws IllegalArgumentException,
                                                NullPointerException {
        int nextToken = text.indexOf("(cost=") + 6;
        if (nextToken < 6) {
            throw (new IllegalArgumentException("Invalid EXPLAIN string: no \"(cost=\""));
        }
        int endOfString = text.indexOf(')', nextToken);
        if (endOfString < 0) {
            throw (new IllegalArgumentException("Invalid EXPLAIN string: no \")\""));
        }
        text = text.substring(nextToken, endOfString);
        nextToken = text.indexOf("..");
        if (nextToken < 0) {
            throw (new IllegalArgumentException("Invalid EXPLAIN string: no \"..\""));
        }
        start = Long.parseLong(text.substring(0, text.indexOf('.'))
                               + text.substring(text.indexOf('.') + 1, nextToken)) / 10;
        text = text.substring(nextToken + 2);
        nextToken = text.indexOf(" rows=");
        if (nextToken < 0) {
            throw (new IllegalArgumentException("Invalid EXPLAIN string: no \" rows=\""));
        }
        complete = Long.parseLong(text.substring(0, text.indexOf('.'))
                                  + text.substring(text.indexOf('.') + 1, nextToken)) / 10;
        text = text.substring(nextToken + 6);
        nextToken = text.indexOf(" width=");
        if (nextToken < 0) {
            throw (new IllegalArgumentException("Invalid EXPLAIN string: no \" width=\""));
        }
        rows = Long.parseLong(text.substring(0, nextToken));
        estimatedRows = rows;
        text = text.substring(nextToken + 7);
        width = Long.parseLong(text);
    }
}
