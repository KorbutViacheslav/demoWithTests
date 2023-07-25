SET search_path TO users;
CREATE OR REPLACE FUNCTION check_employees_email()
    RETURNS TRIGGER
AS
$$
BEGIN
    IF NEW.email IS NULL THEN
        RAISE EXCEPTION 'Email cannot be null';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_user_email_null
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION check_employees_email();

--Trigger audit deleting passports
--Create new table from audit
CREATE TABLE audit_passports_delete
(
    id          SERIAL PRIMARY KEY,
    stamp       timestamp NOT NULL,
    user_id     BIGINT    NOT NULL,
    passport_id BIGINT    NOT NULL
);

--Create new function to insert info in audit_passports_delete table
CREATE OR REPLACE FUNCTION write_audit_delete_passports()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO audit_passports_delete(stamp, user_id, passport_id)
    SELECT now(), OLD.id, NEW.previous_passport_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Create trigger
CREATE TRIGGER trigger_audit_passports_delete
    BEFORE UPDATE
    ON passports
    FOR EACH ROW
    WHEN (NEW.previous_passport_id IS NOT NULL AND OLD.previous_passport_id IS NULL)
EXECUTE FUNCTION write_audit_delete_passports();

--Trigger replaces delete photo with boolean operation
--Add new column to photo table
ALTER TABLE photo
    add is_deleted BOOLEAN DEFAULT FALSE;
--Created function change deleted photo
CREATE OR REPLACE FUNCTION change_delete_photo()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO photo (id, data, name, is_deleted)
    VALUES (OLD.id, OLD.data, OLD.name, TRUE);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;
--Created trigger change deleted photo
CREATE TRIGGER trigger_change_delete_photo
    AFTER DELETE
    ON photo
    FOR EACH ROW
EXECUTE FUNCTION change_delete_photo();
