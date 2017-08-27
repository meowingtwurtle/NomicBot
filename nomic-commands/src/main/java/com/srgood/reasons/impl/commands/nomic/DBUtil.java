package com.srgood.reasons.impl.commands.nomic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DBUtil {
    private static final String PROPOSALS_TABLE_NAME = "proposals";
    private static final String VOTES_TABLE_NAME = "votes";
    private static final String RULES_TABLE_NAME = "rules";
    private static final int INITIAL_PROP_NUMBER = 301;

    private static Connection dbConnection;

    private static PreparedStatement selectProposalStatement;
    private static PreparedStatement selectAllProposalsStatement;
    private static PreparedStatement selectProposalByMessageIDStatement;
    private static PreparedStatement writeProposalStatement;
    private static PreparedStatement selectRuleStatement;
    private static PreparedStatement selectAllRulesStatement;
    private static PreparedStatement writeRuleStatement;
    private static PreparedStatement deleteRuleStatement;
    private static PreparedStatement setRuleMessageStatement;
    private static PreparedStatement countVotesByChoiceStatement;
    private static PreparedStatement selectVotersByVoteStatement;
    private static PreparedStatement writeSingleVoteStatement;
    private static PreparedStatement deleteSingleVoteStatement;

    private static AtomicInteger nextPropID;

    static {
        initDB();
    }

    private static void initDB() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            dbConnection = DriverManager.getConnection("jdbc:hsqldb:file:data/nomic-db", "SA", "");
            ensureTablesExist();
            setupPreparedStatements();
            setupPropIDSystem();
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Nomic database!", e);
        }
    }

    private static void setupPropIDSystem() throws SQLException {
        ResultSet maxPropNumberResultSet = dbConnection.prepareStatement("SELECT MAX(NUMBER) FROM " + PROPOSALS_TABLE_NAME, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();
        if (maxPropNumberResultSet.first()) {
            int returnedMax = maxPropNumberResultSet.getInt(1);
            nextPropID = new AtomicInteger(returnedMax > 0 ? returnedMax + 1 : INITIAL_PROP_NUMBER);
        } else {
            nextPropID = new AtomicInteger(INITIAL_PROP_NUMBER);
        }
    }

    private static void ensureTablesExist() {
        try {
            dbConnection.createStatement()
                        .execute("CREATE TABLE IF NOT EXISTS " + PROPOSALS_TABLE_NAME + " (NUMBER smallint PRIMARY KEY NOT NULL, MESSAGE_ID bigint, TEXT varchar(2000), NUMBER_AFFECTED smallint NOT NULL, STATE smallint NOT NULL, TYPE smallint NOT NULL, VOTE_CLOSING timestamp, PROPOSER_ID bigint DEFAULT 348257765896224779)");
            dbConnection.createStatement()
                        .execute("CREATE TABLE IF NOT EXISTS " + VOTES_TABLE_NAME + " (NUMBER smallint NOT NULL, VOTER_ID bigint NOT NULL, VALUE smallint NOT NULL, PRIMARY KEY(NUMBER, VOTER_ID))");
            dbConnection.createStatement()
                        .execute("CREATE TABLE IF NOT EXISTS " + RULES_TABLE_NAME + " (NUMBER smallint PRIMARY KEY NOT NULL, MESSAGE_ID bigint, MUTABILITY smallint NOT NULL)");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not verify that tables exist.", e);
        }
    }

    private static void setupPreparedStatements() {
        try {
            initializeProposalStatements();
            initializeRuleStatements();
            initializeVoteStatements();
        } catch (Exception e) {
            throw new IllegalStateException("Could not setup prepared statements.", e);
        }
    }

    private static void initializeProposalStatements() {
        try {
            selectProposalStatement = dbConnection.prepareStatement("SELECT * FROM " + PROPOSALS_TABLE_NAME + " WHERE NUMBER = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            selectAllProposalsStatement = dbConnection.prepareStatement("SELECT * FROM " + PROPOSALS_TABLE_NAME + " ORDER BY NUMBER ASC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            selectProposalByMessageIDStatement = dbConnection.prepareStatement("SELECT * FROM " + PROPOSALS_TABLE_NAME + " WHERE MESSAGE_ID = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            writeProposalStatement = dbConnection.prepareStatement("MERGE INTO " + PROPOSALS_TABLE_NAME + " AS oldVals " +
                    "USING (VALUES(?,?,?,?,?,?,?,?)) AS newVals(NUMBER,MESSAGE_ID,TEXT,NUMBER_AFFECTED,STATE,TYPE,VOTE_CLOSING,PROPOSER_ID) " +
                    "ON oldVals.NUMBER = newVals.NUMBER " +
                    "WHEN MATCHED THEN UPDATE SET oldVals.STATE = newVals.STATE, oldVals.MESSAGE_ID = newVals.MESSAGE_ID " +
                    "WHEN NOT MATCHED THEN INSERT VALUES newVals.NUMBER, newVals.MESSAGE_ID, newVals.TEXT, newVals.NUMBER_AFFECTED, newVals.STATE, newVals.TYPE, newVals.VOTE_CLOSING, newVals.PROPOSER_ID", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not setup proposal prepared statements", e);
        }
    }

    private static void initializeRuleStatements() {
        try {
            selectRuleStatement = dbConnection.prepareStatement("SELECT * FROM " + RULES_TABLE_NAME + " WHERE NUMBER = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            selectAllRulesStatement = dbConnection.prepareStatement("SELECT * FROM " + RULES_TABLE_NAME + " ORDER BY NUMBER ASC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            writeRuleStatement = dbConnection.prepareStatement("MERGE INTO " + RULES_TABLE_NAME + " AS oldVals " +
                    "USING (VALUES (?,?,?)) AS newVals(NUMBER,MESSAGE_ID,MUTABILITY) " +
                    "ON oldVals.NUMBER = newVals.NUMBER " +
                    "WHEN MATCHED THEN UPDATE SET oldVals.MESSAGE_ID = newVals.MESSAGE_ID " +
                    "WHEN NOT MATCHED THEN INSERT VALUES newVals.NUMBER, newVals.MESSAGE_ID, newVals.MUTABILITY", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            deleteRuleStatement = dbConnection.prepareStatement("DELETE FROM " + RULES_TABLE_NAME + " WHERE NUMBER = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            setRuleMessageStatement = dbConnection.prepareStatement("UPDATE " + RULES_TABLE_NAME + " SET MESSAGE_ID = ? WHERE NUMBER = ?");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not setup rule prepared statements", e);
        }
    }

    private static void initializeVoteStatements() {
        try {
            countVotesByChoiceStatement = dbConnection.prepareStatement("SELECT COUNT(VOTER_ID) FROM " + VOTES_TABLE_NAME + " WHERE NUMBER = ? AND VALUE = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            selectVotersByVoteStatement = dbConnection.prepareStatement("SELECT VOTER_ID FROM " + VOTES_TABLE_NAME + " WHERE NUMBER = ? AND VALUE = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            writeSingleVoteStatement = dbConnection.prepareStatement(
                    "MERGE INTO " + VOTES_TABLE_NAME + " AS oldVals " +
                            "USING (VALUES(?,?,?)) AS newVals(NUMBER,VOTER_ID,VALUE) " +
                            "ON oldVals.NUMBER = newVals.NUMBER AND oldVals.VOTER_ID = newVals.VOTER_ID " +
                            "WHEN MATCHED THEN UPDATE SET oldVals.VALUE = newVals.VALUE " +
                            "WHEN NOT MATCHED THEN INSERT VALUES newVals.NUMBER, newVals.VOTER_ID, newVals.VALUE", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            deleteSingleVoteStatement = dbConnection.prepareStatement("DELETE FROM " + VOTES_TABLE_NAME + " WHERE NUMBER = ? AND VOTER_ID = ?");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not setup vote prepared statements", e);
        }
    }

    public static Proposal getProposalByID(int id) {
        try {
            selectProposalStatement.setInt(1, id);
            ResultSet resultSet = selectProposalStatement.executeQuery();
            selectProposalStatement.clearParameters();

            if (!resultSet.first()) {
                return null;
            }

            return resultSetToProposal(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading proposal", e);
        }
    }

    public static Proposal getProposalByMessageID(long messageID) {
        try {
            selectProposalByMessageIDStatement.setLong(1, messageID);
            ResultSet resultSet = selectProposalByMessageIDStatement.executeQuery();
            selectProposalByMessageIDStatement.clearParameters();
            resultSet.first();
            return resultSetToProposal(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading proposals", e);
        }
    }

    public static List<Proposal> getAllProposals() {
        try {
            ResultSet resultSet = selectAllProposalsStatement.executeQuery();
            List<Proposal> proposals = new ArrayList<>();
            for (resultSet.beforeFirst(); resultSet.next();) {
                proposals.add(resultSetToProposal(resultSet));
            }
            return proposals;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read proposals", e);
        }
    }

    private static Proposal resultSetToProposal(ResultSet resultSet) {
        try {
            if (resultSet.getRow() == 0) return null;
            int number = resultSet.getInt("NUMBER");
            long messageID = resultSet.getLong("MESSAGE_ID");
            long proposerID = resultSet.getLong("PROPOSER_ID");
            String text = resultSet.getString("TEXT");
            int numberAffected = resultSet.getInt("NUMBER_AFFECTED");
            ProposalState state = intToState(resultSet.getInt("STATE"));
            ProposalType type = intToType(resultSet.getInt("TYPE"));
            Timestamp voteExpiry = resultSet.getTimestamp("VOTE_CLOSING");
            return state == ProposalState.VOTING ? new ProposalVoting(number, messageID, proposerID, text, numberAffected, type, voteExpiry) : new Proposal(number, messageID, proposerID, text, numberAffected, state, type);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error parsing resultset to proposal", e);
        }
    }

    private static ProposalState intToState(int stateInt) {
        for (ProposalState proposalState : ProposalState.values()) {
            if (proposalState.getID() == stateInt) {
                return proposalState;
            }
        }
        throw new IllegalArgumentException();
    }

    private static ProposalType intToType(int typeInt) {
        for (ProposalType proposalType : ProposalType.values()) {
            if (proposalType.getID() == typeInt) {
                return proposalType;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void writeProposal(Proposal proposal) {
        try {
            writeProposalStatement.setInt(1, proposal.getNumber());
            writeProposalStatement.setLong(2, proposal.getMessageID());
            writeProposalStatement.setString(3, proposal.getText());
            writeProposalStatement.setInt(4, proposal.getAffectedRule());
            writeProposalStatement.setInt(5, proposal.getState().getID());
            writeProposalStatement.setInt(6, proposal.getType().getID());
            writeProposalStatement.setTimestamp(7, proposal instanceof ProposalVoting ? ((ProposalVoting) proposal).voteExpiry() : null);
            writeProposalStatement.setLong(8, proposal.getProposerID());
            writeProposalStatement.execute();
            writeProposalStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error writing proposal.", e);
        }
    }

    public static Rule getRuleByID(int id) {
        try {
            selectRuleStatement.setInt(1, id);
            ResultSet resultSet = selectRuleStatement.executeQuery();
            selectRuleStatement.clearParameters();

            if (!resultSet.first()) {
                return null;
            }

            return resultSetToRule(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading rule.", e);
        }
    }

    public static List<Rule> getAllRules() {
        try {
            ResultSet resultSet = selectAllRulesStatement.executeQuery();
            List<Rule> ruleList = new ArrayList<>();
            for (resultSet.beforeFirst(); resultSet.next();) {
                ruleList.add(resultSetToRule(resultSet));
            }
            return ruleList;
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading rules.", e);
        }
    }

    private static Rule resultSetToRule(ResultSet resultSet) throws SQLException {
        try {
            if (resultSet.getRow() == 0) return null;
            int number = resultSet.getInt("NUMBER");
            long messageID = resultSet.getLong("MESSAGE_ID");
            int mutability = resultSet.getInt("MUTABILITY");
            return new Rule(number, messageID, intToMutability(mutability));
        } catch (SQLException e) {
            throw new IllegalStateException("Error parsing resultset to rule.", e);
        }
    }

    private static RuleMutability intToMutability(int mutabilityInt) {
        for (RuleMutability mutability : RuleMutability.values()) {
            if (mutability.getID() == mutabilityInt) {
                return mutability;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void writeRule(Rule rule) {
        try {
            writeRuleStatement.setInt(1, rule.getNumber());
            writeRuleStatement.setLong(2, rule.getMessageID());
            writeRuleStatement.setInt(3, rule.getMutability().getID());
            writeRuleStatement.execute();
            writeRuleStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error writing rule.", e);
        }
    }

    public static void writeVote(int number, long discordID, VoteType vote) {
        try {
            writeSingleVoteStatement.setInt(1, number);
            writeSingleVoteStatement.setLong(2, discordID);
            writeSingleVoteStatement.setInt(3, vote.value());
            writeSingleVoteStatement.execute();
            writeSingleVoteStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error writing vote.", e);
        }
    }

    public static void removeVote(int number, long discordID) {
        try {
            deleteSingleVoteStatement.setInt(1, number);
            deleteSingleVoteStatement.setLong(2, discordID);
            deleteSingleVoteStatement.execute();
            deleteSingleVoteStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error removing vote.", e);
        }
    }

    public static void removeRule(int number) {
        try {
            deleteRuleStatement.setInt(1, number);
            deleteRuleStatement.execute();
            deleteRuleStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error removing rule.", e);
        }
    }

    public static void setRuleMessageID(int ruleNumber, long messageID) {
        try {
            setRuleMessageStatement.setLong(1, messageID);
            setRuleMessageStatement.setInt(2, ruleNumber);
            setRuleMessageStatement.execute();
            setRuleMessageStatement.clearParameters();
        } catch (SQLException e) {
            throw new IllegalStateException("Error accessing rule.", e);
        }
    }

    public static int getYeaVoteCount(int number) {
        return getVotesCountByVoteType(number, VoteType.YEA);
    }

    public static int getNayVoteCount(int number) {
        return getVotesCountByVoteType(number, VoteType.NAY);
    }

    public static int getVotesCountByVoteType(int number, VoteType vote) {
        try {
            countVotesByChoiceStatement.setInt(1, number);
            countVotesByChoiceStatement.setInt(2, vote.value());
            ResultSet resultSet = countVotesByChoiceStatement.executeQuery();
            countVotesByChoiceStatement.clearParameters();
            return resultSet.first() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading votes.", e);
        }
    }

    public static List<Long> getVotersByVoteType(int number, VoteType vote) {
        try {
            selectVotersByVoteStatement.setInt(1, number);
            selectVotersByVoteStatement.setInt(2, vote.value());
            ResultSet resultSet = selectVotersByVoteStatement.executeQuery();
            selectVotersByVoteStatement.clearParameters();

            List<Long> ret = new ArrayList<>();
            for (resultSet.beforeFirst(); resultSet.next();) {
                ret.add(resultSet.getLong("VOTER_ID"));
            }
            return ret;
        } catch (SQLException e) {
            throw new IllegalStateException("Error reading votes", e);
        }
    }

    public static int getNextProposalID() {
        return nextPropID.getAndIncrement();
    }

    public static void shutdownDB() {
        try {
            dbConnection.createStatement().execute("SHUTDOWN");
        } catch (SQLException e) {
            throw new RuntimeException("Error shutting down", e);
        }
    }
}
