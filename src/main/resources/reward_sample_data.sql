-- Sample data for testing reward system
-- This script initializes sample reward points and transactions for testing

-- Initialize reward points for existing accounts
-- Assuming accounts with IDs 1 and 2 exist from previous setup
INSERT INTO reward_points (account_id, total_points, created_on, updated_on)
SELECT id, 0 AS total_points, NOW() AS created_on, NOW() AS updated_on
FROM account
WHERE id NOT IN (SELECT account_id FROM reward_points);

-- Example: View reward points for account ID 1
-- SELECT * FROM reward_points WHERE account_id = 1;

-- Example: View reward transactions for account ID 1
-- SELECT * FROM reward_transactions WHERE from_account_id = 1 ORDER BY created_on DESC;

-- Clean up old test data (optional - comment out if needed)
-- DELETE FROM reward_transactions WHERE created_on < DATE_SUB(NOW(), INTERVAL 7 DAY);
