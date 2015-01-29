// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


// base extension
import org.melati.poem.Setting;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
// ours
//import org.paneris.melati.boards.model.Setting;
// ours
//import org.paneris.melati.boards.model.SettingTable;


/**
 * Melati POEM generated abstract base class for a <code>Persistent</code> 
 * <code>Setting</code> Object.
 *
 * see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
 */
public abstract class SettingBase extends org.melati.poem.Setting {


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
  * Retrieves the  <code>SettingTable</code> table 
  * which this <code>Persistent</code> is from.
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentBaseJava 
  * @return the org.melati.poem.SettingTable
  */
  @SuppressWarnings("unchecked")
  public org.melati.poem.SettingTable<org.melati.poem.Setting> getSettingTable() {
    return (org.melati.poem.SettingTable<org.melati.poem.Setting>)getTable();
  }

  // There are no Fields in this table, only in its ancestors 

}

