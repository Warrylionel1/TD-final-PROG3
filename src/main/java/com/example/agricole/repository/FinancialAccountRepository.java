package com.example.agricole.repository;

import com.example.agricole.entity.*;
import com.example.agricole.enums.Bank;
import com.example.agricole.enums.MobileBankingService;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialAccountRepository {
    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(FinancialAccount account) {
        if (account.getId() == null) {
            account.setId(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO financial_account (id, collectivity_id, type, amount, holder_name, mobile_banking_service, mobile_number, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getId());
            ps.setString(2, account.getCollectivityId());
            ps.setString(3, account.getClass().getSimpleName().toUpperCase());
            ps.setDouble(4, account.getAmount());

            if (account instanceof MobileBankingAccount) {
                MobileBankingAccount mb = (MobileBankingAccount) account;
                ps.setString(5, mb.getHolderName());
                ps.setString(6, mb.getMobileBankingService().name());
                ps.setString(7, mb.getMobileNumber());
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
                ps.setNull(11, Types.INTEGER);
                ps.setNull(12, Types.INTEGER);
            } else if (account instanceof BankAccount) {
                BankAccount ba = (BankAccount) account;
                ps.setString(5, ba.getHolderName());
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, ba.getBankName().name());
                ps.setInt(9, ba.getBankCode());
                ps.setInt(10, ba.getBankBranchCode());
                ps.setInt(11, ba.getBankAccountNumber());
                ps.setInt(12, ba.getBankAccountKey());
            } else { // CashAccount
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
                ps.setNull(11, Types.INTEGER);
                ps.setNull(12, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving financial account", e);
        }
    }

    public Optional<FinancialAccount> findById(String id) {
        String sql = "SELECT * FROM financial_account WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding financial account", e);
        }
    }

    public void updateAmount(String id, double newAmount) {
        String sql = "UPDATE financial_account SET amount = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account amount", e);
        }
    }

    private FinancialAccount mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String id = rs.getString("id");
        String collectivityId = rs.getString("collectivity_id");
        double amount = rs.getDouble("amount");

        if ("CASH".equals(type)) {
            CashAccount acc = new CashAccount();
            acc.setId(id);
            acc.setCollectivityId(collectivityId);
            acc.setAmount(amount);
            return acc;
        } else if ("MOBILE_BANKING".equals(type)) {
            MobileBankingAccount acc = new MobileBankingAccount();
            acc.setId(id);
            acc.setCollectivityId(collectivityId);
            acc.setAmount(amount);
            acc.setHolderName(rs.getString("holder_name"));
            acc.setMobileBankingService(MobileBankingService.valueOf(rs.getString("mobile_banking_service")));
            acc.setMobileNumber(rs.getString("mobile_number"));
            return acc;
        } else if ("BANK".equals(type)) {
            BankAccount acc = new BankAccount();
            acc.setId(id);
            acc.setCollectivityId(collectivityId);
            acc.setAmount(amount);
            acc.setHolderName(rs.getString("holder_name"));
            acc.setBankName(Bank.valueOf(rs.getString("bank_name")));
            acc.setBankCode(rs.getInt("bank_code"));
            acc.setBankBranchCode(rs.getInt("bank_branch_code"));
            acc.setBankAccountNumber(rs.getInt("bank_account_number"));
            acc.setBankAccountKey(rs.getInt("bank_account_key"));
            return acc;
        }
        throw new IllegalArgumentException("Unknown account type: " + type);
    }
}