package org.paneris.melati.boards.model;


import org.paneris.melati.boards.model.generated.AttachmentTypeTableBase;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.PoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>AttachmentTypeTable</code> object.
 * <p>
 * Description: 
 *   A type of an attachment. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>AttachmentType</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> type </td><td> String </td><td> The name of a type </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class AttachmentTypeTable<T extends AttachmentType> extends AttachmentTypeTableBase<AttachmentType> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public AttachmentTypeTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here

  /**
   * Make sure that a value is in the table.
   * 
   * @param contentType  a <tt>contentType</tt> to ensure.
   * @return the existing or newly created <tt>AttachmentType</tt>.
   */
   public AttachmentType ensure(String contentType) {
     AttachmentType newType = (AttachmentType)newPersistent();
     newType.setType_unsafe(contentType.toLowerCase());
     return (AttachmentType)getColumn("type").ensure(newType);
   }
}

