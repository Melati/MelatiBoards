// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Field;
import org.melati.poem.JdbcPersistent;
import org.melati.poem.NoSuchRowPoemException;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.MembershipStatus;
import org.paneris.melati.boards.model.SubscriptionTable;
import org.paneris.melati.boards.model.User;


/**
 * Melati POEM generated abstract base class for a <code>Persistent</code> 
 * <code>Subscription</code> Object.
 *
 * @see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
 */
public abstract class SubscriptionBase extends JdbcPersistent {


 /**
  * Retrieves the Database object.
  * 
  * @see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
  * @return the database
  */
  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }


 /**
  * Retrieves the  <code>SubscriptionTable</code> table 
  * which this <code>Persistent</code> is from.
  * 
  * @see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
  * @return the SubscriptionTable
  */
  public SubscriptionTable getSubscriptionTable() {
    return (SubscriptionTable)getTable();
  }

  private SubscriptionTable _getSubscriptionTable() {
    return (SubscriptionTable)getTable();
  }

  // Fields in this table 
 /**
  * id 
  */
  protected Integer id;
 /**
  * user - The user 
  */
  protected Integer user;
 /**
  * board - The board to which this user belongs 
  */
  protected Integer board;
 /**
  * status - How users would like to receive emails from this board 
  */
  protected Integer status;
 /**
  * Is A Manager - Can the user administrator the board with manager 
  * priviledges? 
  */
  protected Boolean ismanager;
 /**
  * Approved - Has this user's subscription to this board been approved by a 
  * manager (if the board has moderated subscription) 
  */
  protected Boolean approved;


 /**
  * Retrieves the <code>Id</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer id
  */
  public Integer getId_unsafe() {
    return id;
  }


 /**
  * Sets the <code>Id</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setId_unsafe(Integer cooked) {
    id = cooked;
  }

 /**
  * Retrieves the Id value, with locking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Id</code> for this 
  *         <code>Subscription</code> <code>Persistent</code>  
  */

  public Integer getId()
      throws AccessPoemException {
    readLock();
    return getId_unsafe();
  }


 /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setId(Integer cooked)
      throws AccessPoemException, ValidationPoemException {
    _getSubscriptionTable().getIdColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setId_unsafe(cooked);
  }

 /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.IntegerFieldDef#generateBaseMethods 
  * @param cooked  a validated <code>int</code>
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */

  public final void setId(int cooked)
      throws AccessPoemException, ValidationPoemException {
    setId(new Integer(cooked));
  }


 /**
  * Retrieves the <code>Id</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer id
  */
  public Field<Integer> getIdField() throws AccessPoemException {
    Column<Integer> c = _getSubscriptionTable().getIdColumn();
    return new Field<Integer>((Integer)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>User</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer user
  */
  public Integer getUser_unsafe() {
    return user;
  }


 /**
  * Sets the <code>User</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setUser_unsafe(Integer cooked) {
    user = cooked;
  }

 /**
  * Retrieves the Table Row Object ID. 
  *
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @return the TROID as an <code>Integer</code> 
  */

  public Integer getUserTroid()
      throws AccessPoemException {
    readLock();
    return getUser_unsafe();
  }


 /**
  * Sets the Table Row Object ID. 
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param raw  a Table Row Object Id 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  */
  public void setUserTroid(Integer raw)
      throws AccessPoemException {
    setUser(raw == null ? null : 
        (User)getBoardsDatabaseTables().getUserTable().getUserObject(raw));
  }


 /**
  * Retrieves the <code>User</code> object referred to.
  *  
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @throws NoSuchRowPoemException  
  *         if the <code>Persistent</code> has yet to be allocated a TROID 
  * @return the <code>User</code> as a <code>User</code> 
  */
  public User getUser()
      throws AccessPoemException, NoSuchRowPoemException {
    Integer troid = getUserTroid();
    return troid == null ? null :
        (User)getBoardsDatabaseTables().getUserTable().getUserObject(troid);
  }


 /**
  * Set the User.
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param cooked  a validated <code>User</code>
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  */
  public void setUser(User cooked)
      throws AccessPoemException {
    _getSubscriptionTable().
      getUserColumn().
        getType().assertValidCooked(cooked);
    writeLock();
    if (cooked == null)
      setUser_unsafe(null);
    else {
      cooked.existenceLock();
      setUser_unsafe(cooked.troid());
    }
  }


 /**
  * Retrieves the <code>User</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer user
  */
  public Field<Integer> getUserField() throws AccessPoemException {
    Column<Integer> c = _getSubscriptionTable().getUserColumn();
    return new Field<Integer>((Integer)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Board</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer board
  */
  public Integer getBoard_unsafe() {
    return board;
  }


 /**
  * Sets the <code>Board</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setBoard_unsafe(Integer cooked) {
    board = cooked;
  }

 /**
  * Retrieves the Table Row Object ID. 
  *
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @return the TROID as an <code>Integer</code> 
  */

  public Integer getBoardTroid()
      throws AccessPoemException {
    readLock();
    return getBoard_unsafe();
  }


 /**
  * Sets the Table Row Object ID. 
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param raw  a Table Row Object Id 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  */
  public void setBoardTroid(Integer raw)
      throws AccessPoemException {
    setBoard(raw == null ? null : 
        getBoardsDatabaseTables().getBoardTable().getBoardObject(raw));
  }


 /**
  * Retrieves the <code>Board</code> object referred to.
  *  
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @throws NoSuchRowPoemException  
  *         if the <code>Persistent</code> has yet to be allocated a TROID 
  * @return the <code>Board</code> as a <code>Board</code> 
  */
  public Board getBoard()
      throws AccessPoemException, NoSuchRowPoemException {
    Integer troid = getBoardTroid();
    return troid == null ? null :
        getBoardsDatabaseTables().getBoardTable().getBoardObject(troid);
  }


 /**
  * Set the Board.
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param cooked  a validated <code>Board</code>
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  */
  public void setBoard(Board cooked)
      throws AccessPoemException {
    _getSubscriptionTable().
      getBoardColumn().
        getType().assertValidCooked(cooked);
    writeLock();
    if (cooked == null)
      setBoard_unsafe(null);
    else {
      cooked.existenceLock();
      setBoard_unsafe(cooked.troid());
    }
  }


 /**
  * Retrieves the <code>Board</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer board
  */
  public Field<Integer> getBoardField() throws AccessPoemException {
    Column<Integer> c = _getSubscriptionTable().getBoardColumn();
    return new Field<Integer>((Integer)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Status</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer status
  */
  public Integer getStatus_unsafe() {
    return status;
  }


 /**
  * Sets the <code>Status</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setStatus_unsafe(Integer cooked) {
    status = cooked;
  }

 /**
  * Retrieves the Table Row Object ID. 
  *
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @return the TROID as an <code>Integer</code> 
  */

  public Integer getStatusTroid()
      throws AccessPoemException {
    readLock();
    return getStatus_unsafe();
  }


 /**
  * Sets the Table Row Object ID. 
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param raw  a Table Row Object Id 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  */
  public void setStatusTroid(Integer raw)
      throws AccessPoemException {
    setStatus(raw == null ? null : 
        getBoardsDatabaseTables().getMembershipStatusTable().getMembershipStatusObject(raw));
  }


 /**
  * Retrieves the <code>Status</code> object referred to.
  *  
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer read access rights 
  * @throws NoSuchRowPoemException  
  *         if the <code>Persistent</code> has yet to be allocated a TROID 
  * @return the <code>Status</code> as a <code>MembershipStatus</code> 
  */
  public MembershipStatus getStatus()
      throws AccessPoemException, NoSuchRowPoemException {
    Integer troid = getStatusTroid();
    return troid == null ? null :
        getBoardsDatabaseTables().getMembershipStatusTable().getMembershipStatusObject(troid);
  }


 /**
  * Set the Status.
  * 
  * @generator org.melati.poem.prepro.ReferenceFieldDef#generateBaseMethods 
  * @param cooked  a validated <code>MembershipStatus</code>
  * @throws AccessPoemException  
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  */
  public void setStatus(MembershipStatus cooked)
      throws AccessPoemException {
    _getSubscriptionTable().
      getStatusColumn().
        getType().assertValidCooked(cooked);
    writeLock();
    if (cooked == null)
      setStatus_unsafe(null);
    else {
      cooked.existenceLock();
      setStatus_unsafe(cooked.troid());
    }
  }


 /**
  * Retrieves the <code>Status</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer status
  */
  public Field<Integer> getStatusField() throws AccessPoemException {
    Column<Integer> c = _getSubscriptionTable().getStatusColumn();
    return new Field<Integer>((Integer)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Ismanager</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Boolean ismanager
  */
  public Boolean getIsmanager_unsafe() {
    return ismanager;
  }


 /**
  * Sets the <code>Ismanager</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setIsmanager_unsafe(Boolean cooked) {
    ismanager = cooked;
  }

 /**
  * Retrieves the Ismanager value, with locking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Can the user administrator the board with manager priviledges? 
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Ismanager</code> for this 
  *         <code>Subscription</code> <code>Persistent</code>  
  */

  public Boolean getIsmanager()
      throws AccessPoemException {
    readLock();
    return getIsmanager_unsafe();
  }


 /**
  * Sets the <code>Ismanager</code> value, with checking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Can the user administrator the board with manager priviledges? 
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setIsmanager(Boolean cooked)
      throws AccessPoemException, ValidationPoemException {
    _getSubscriptionTable().getIsmanagerColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setIsmanager_unsafe(cooked);
  }

 /**
  * Sets the <code>Ismanager</code> value, with checking, 
  * from a <code>boolean</code>, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Can the user administrator the board with manager priviledges? 
  * 
  * 
  * @generator org.melati.poem.prepro.BooleanFieldDef#generateBaseMethods 
  * @param cooked  a <code>boolean</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */

  public final void setIsmanager(boolean cooked)
      throws AccessPoemException, ValidationPoemException {
    setIsmanager(cooked ? Boolean.TRUE : Boolean.FALSE);
  }


 /**
  * Retrieves the <code>Ismanager</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Boolean ismanager
  */
  public Field<Boolean> getIsmanagerField() throws AccessPoemException {
    Column<Boolean> c = _getSubscriptionTable().getIsmanagerColumn();
    return new Field<Boolean>((Boolean)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Approved</code> value, without locking, 
  * for this <code>Subscription</code> <code>Persistent</code>.
  *
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Boolean approved
  */
  public Boolean getApproved_unsafe() {
    return approved;
  }


 /**
  * Sets the <code>Approved</code> value directly, without checking, 
  * for this Subscription <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setApproved_unsafe(Boolean cooked) {
    approved = cooked;
  }

 /**
  * Retrieves the Approved value, with locking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Has this user's subscription to this board been approved by a manager 
  *   (if the board has moderated subscription) 
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Approved</code> for this 
  *         <code>Subscription</code> <code>Persistent</code>  
  */

  public Boolean getApproved()
      throws AccessPoemException {
    readLock();
    return getApproved_unsafe();
  }


 /**
  * Sets the <code>Approved</code> value, with checking, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Has this user's subscription to this board been approved by a manager 
  *   (if the board has moderated subscription) 
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setApproved(Boolean cooked)
      throws AccessPoemException, ValidationPoemException {
    _getSubscriptionTable().getApprovedColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setApproved_unsafe(cooked);
  }

 /**
  * Sets the <code>Approved</code> value, with checking, 
  * from a <code>boolean</code>, for this 
  * <code>Subscription</code> <code>Persistent</code>.
  * Field description: 
  *   Has this user's subscription to this board been approved by a manager 
  *   (if the board has moderated subscription) 
  * 
  * 
  * @generator org.melati.poem.prepro.BooleanFieldDef#generateBaseMethods 
  * @param cooked  a <code>boolean</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */

  public final void setApproved(boolean cooked)
      throws AccessPoemException, ValidationPoemException {
    setApproved(cooked ? Boolean.TRUE : Boolean.FALSE);
  }


 /**
  * Retrieves the <code>Approved</code> value as a <code>Field</code>
  * from this <code>Subscription</code> <code>Persistent</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Boolean approved
  */
  public Field<Boolean> getApprovedField() throws AccessPoemException {
    Column<Boolean> c = _getSubscriptionTable().getApprovedColumn();
    return new Field<Boolean>((Boolean)c.getRaw(this), c);
  }

}

