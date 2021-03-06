// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.melati.poem.AccessPoemException;
import org.melati.poem.CachedSelection;
import org.melati.poem.Column;
import org.melati.poem.Field;
import org.melati.poem.JdbcPersistent;
import org.melati.poem.ValidationPoemException;
import org.melati.poem.util.EmptyEnumeration;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardType;
import org.paneris.melati.boards.model.BoardTypeTable;
import org.paneris.melati.boards.model.BoardsDatabaseTables;


/**
 * Melati POEM generated abstract base class for a <code>Persistent</code> 
 * <code>BoardType</code> Object.
 *
 * see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
 */
public abstract class BoardTypeBase extends JdbcPersistent {


 /**
  * Retrieves the Database object.
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
  * @return the database
  */
  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }


 /**
  * Retrieves the  <code>BoardTypeTable</code> table 
  * which this <code>Persistent</code> is from.
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
  * @return the BoardTypeTable
  */
  @SuppressWarnings("unchecked")
  public BoardTypeTable<BoardType> getBoardTypeTable() {
    return (BoardTypeTable<BoardType>)getTable();
  }

  @SuppressWarnings("unchecked")
  private BoardTypeTable<BoardType> _getBoardTypeTable() {
    return (BoardTypeTable<BoardType>)getTable();
  }

  // Fields in this table 
 /**
  * id 
  */
  protected Integer id;
 /**
  * type - The name of a type 
  */
  protected String type;
 /**
  * description - The description of the type 
  */
  protected String description;


 /**
  * Retrieves the <code>Id</code> value, without locking, 
  * for this <code>BoardType</code> <code>Persistent</code>.
  *
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer id
  */
  public Integer getId_unsafe() {
    return id;
  }


 /**
  * Sets the <code>Id</code> value directly, without checking, 
  * for this BoardType <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setId_unsafe(Integer cooked) {
    id = cooked;
  }

 /**
  * Retrieves the Id value, with locking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Id</code> for this 
  *         <code>BoardType</code> <code>Persistent</code>  
  */

  public Integer getId()
      throws AccessPoemException {
    readLock();
    return getId_unsafe();
  }


 /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setId(Integer cooked)
      throws AccessPoemException, ValidationPoemException {
    _getBoardTypeTable().getIdColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setId_unsafe(cooked);
  }

 /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * 
  * Generated by org.melati.poem.prepro.IntegerFieldDef#generateBaseMethods 
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
  * from this <code>BoardType</code> <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer id
  */
  public Field<Integer> getIdField() throws AccessPoemException {
    Column<Integer> c = _getBoardTypeTable().getIdColumn();
    return new Field<Integer>((Integer)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Type</code> value, without locking, 
  * for this <code>BoardType</code> <code>Persistent</code>.
  *
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the String type
  */
  public String getType_unsafe() {
    return type;
  }


 /**
  * Sets the <code>Type</code> value directly, without checking, 
  * for this BoardType <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setType_unsafe(String cooked) {
    type = cooked;
  }

 /**
  * Retrieves the Type value, with locking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * Field description: 
  *   The name of a type 
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Type</code> for this 
  *         <code>BoardType</code> <code>Persistent</code>  
  */

  public String getType()
      throws AccessPoemException {
    readLock();
    return getType_unsafe();
  }


 /**
  * Sets the <code>Type</code> value, with checking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * Field description: 
  *   The name of a type 
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setType(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getBoardTypeTable().getTypeColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setType_unsafe(cooked);
  }


 /**
  * Retrieves the <code>Type</code> value as a <code>Field</code>
  * from this <code>BoardType</code> <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the String type
  */
  public Field<String> getTypeField() throws AccessPoemException {
    Column<String> c = _getBoardTypeTable().getTypeColumn();
    return new Field<String>((String)c.getRaw(this), c);
  }


 /**
  * Retrieves the <code>Description</code> value, without locking, 
  * for this <code>BoardType</code> <code>Persistent</code>.
  *
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the String description
  */
  public String getDescription_unsafe() {
    return description;
  }


 /**
  * Sets the <code>Description</code> value directly, without checking, 
  * for this BoardType <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
  public void setDescription_unsafe(String cooked) {
    description = cooked;
  }

 /**
  * Retrieves the Description value, with locking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * Field description: 
  *   The description of the type 
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Description</code> for this 
  *         <code>BoardType</code> <code>Persistent</code>  
  */

  public String getDescription()
      throws AccessPoemException {
    readLock();
    return getDescription_unsafe();
  }


 /**
  * Sets the <code>Description</code> value, with checking, for this 
  * <code>BoardType</code> <code>Persistent</code>.
  * Field description: 
  *   The description of the type 
  * 
  * Generated by org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
  public void setDescription(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getBoardTypeTable().getDescriptionColumn().
      getType().assertValidCooked(cooked);
    writeLock();
    setDescription_unsafe(cooked);
  }


 /**
  * Retrieves the <code>Description</code> value as a <code>Field</code>
  * from this <code>BoardType</code> <code>Persistent</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the String description
  */
  public Field<String> getDescriptionField() throws AccessPoemException {
    Column<String> c = _getBoardTypeTable().getDescriptionColumn();
    return new Field<String>((String)c.getRaw(this), c);
  }

  private CachedSelection<Board> typeBoards = null;
  /** References to this BoardType in the Board table via its type field.*/
  @SuppressWarnings("unchecked")
  public Enumeration<Board> getTypeBoards() {
    if (getTroid() == null)
      return new EmptyEnumeration<Board>();
    else {
      if (typeBoards == null)
        typeBoards =
          getBoardsDatabaseTables().getBoardTable().getTypeColumn().cachedSelectionWhereEq(getTroid());
      return typeBoards.objects();
    }
  }


  /** References to this BoardType in the Board table via its type field, as a List.*/
  public List<Board> getTypeBoardList() {
    return Collections.list(getTypeBoards());
  }



}

