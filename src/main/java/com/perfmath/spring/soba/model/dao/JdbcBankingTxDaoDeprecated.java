package com.perfmath.spring.soba.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import com.perfmath.spring.soba.model.domain.BankingTx;

public class JdbcBankingTxDaoDeprecated extends SimpleJdbcDaoSupport implements BankingTxDao {
    public List<BankingTx> getTransactionList() {
        logger.info("Getting transactions!");
        List<BankingTx> txs = getSimpleJdbcTemplate().query(
                "SELECT TRANSACTION_ID, TRANS_DATE, TYPE, " 
        		+ " INITIATOR, DESCRIPTION, AMOUNT, BALANCE, ACCOUNT_ID, STATUS FROM BANKINGTX", 
                new TransactionMapper());
        return txs;
    }
    public List<BankingTx> getTransactionList(String accountId) {
        logger.info("Getting transactions!");
        List<BankingTx> txs = getSimpleJdbcTemplate().query(
                "SELECT TRANSACTION_ID, TRANS_DATE, TYPE, " 
        		+ " INITIATOR, DESCRIPTION, AMOUNT, BALANCE, " + 
        		" ACCOUNT_ID, STATUS FROM BANKINGTX WHERE ACCOUNT_ID = ?" 
        		+ " ORDER BY TRANS_DATE DESC", 
                new TransactionMapper(), accountId);
        return txs;
    }
	
    public void insert(BankingTx transaction) {
        String sql = "INSERT INTO BANKINGTX (TRANSACTION_ID, TRANS_DATE, TYPE,"
        	+"INITIATOR, DESCRIPTION, AMOUNT, BALANCE, ACCOUNT_ID, STATUS) " 
                + "VALUES (:transactionId, :transDate, :type, :initiator, "
                + ":description, :amount, :balance, :accountId, :status)";
     
        SqlParameterSource parameterSource =
            new BeanPropertySqlParameterSource(transaction);

        int count = getSimpleJdbcTemplate().update(sql, parameterSource);
    }

    public void insertBatch(List<BankingTx> trans) {
        String sql = "INSERT INTO BANKINGTX (TRANSACTION_ID, TRANS_DATE, TYPE,"
        	+"INITIATOR, DESCRIPTION, AMOUNT, BALANCE, ACCOUNT_ID, STATUS ) " 
                + "VALUES (:transactionId, :transDate, :type, :initiator, "
                + ":description, :amount, :balance, :accountId, :status)";

        List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>();
        for (BankingTx Transaction : trans) {
            parameters.add(new BeanPropertySqlParameterSource(Transaction));
        }
        getSimpleJdbcTemplate().batchUpdate(sql,
                parameters.toArray(new SqlParameterSource[0]));
    }

    public BankingTx findByTransactionID(String transID) {
    	
        String sql = "SELECT TRANSACTION_ID, TRANS_DATE, TYPE, " 
        		+ " INITIATOR, DESCRIPTION, AMOUNT, BALANCE, ACCOUNT_ID, STATUS "
        		+ " FROM BANKINGTX WHERE TRANSACTION_ID = '" + transID + "'";
System.out.println ("id = " + transID + " sql = " + sql);
        BankingTx trans = getSimpleJdbcTemplate().queryForObject(sql,
        		 new TransactionMapper());
        		
        return trans;
    }

    public void update(BankingTx tx) {
    }

    public void delete(String txId) {
        String sql = "DELETE BANKINGTX WHERE TRANSACTION_ID = ?";

        int count = getSimpleJdbcTemplate().update(sql, txId);
    }

    public List<Map<String, Object>> findAll() {
        String sql = "SELECT * FROM BANKINGTX";

        List<Map<String,Object>> trans = getSimpleJdbcTemplate().queryForList(sql, BankingTx.class);
        return trans;
    }

    public String getAccountId(String transID) {
        String sql = "SELECT ACCOUNT_ID FROM BANKINGTX WHERE TRANSACTION_ID = ?";

        String accountId = getSimpleJdbcTemplate().queryForObject(sql,
                String.class, transID);
        return accountId;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM BANKINGTX";

        int count = getJdbcTemplate().queryForInt(sql);
        return count;
    }
    private static class TransactionMapper implements ParameterizedRowMapper<BankingTx> {
    	public BankingTx mapRow(ResultSet rs, int rowNum) throws SQLException {
    		BankingTx tx = new BankingTx();
    		tx.setTransactionId(rs.getInt("TRANSACTION_ID"));
    		tx.setTransDate(rs.getTimestamp("TRANS_DATE"));
    		tx.setType(rs.getString("TYPE"));
    		tx.setInitiator(rs.getString("INITIATOR"));
    		tx.setDescription(rs.getString("DESCRIPTION"));
    		tx.setAmount(rs.getDouble("AMOUNT"));
    		tx.setBalance(rs.getDouble("BALANCE"));
    		tx.setAccountId(rs.getString("ACCOUNT_ID"));
    		tx.setStatus(rs.getString("STATUS"));
            return tx;
        }
    }
}

