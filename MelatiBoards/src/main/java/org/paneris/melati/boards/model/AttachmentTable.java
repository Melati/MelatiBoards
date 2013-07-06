// Delete this line to prevent overwriting of this file

package org.paneris.melati.boards.model;


import org.paneris.melati.boards.model.generated.AttachmentTableBase;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.PoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>AttachmentTable</code> object.
 * <p>
 * Description: 
 *   A file sent as an attachment to a message. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Attachment</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> message </td><td> Message </td><td> The message this attachment 
 * belongs to </td></tr> 
 * <tr><td> filename </td><td> String </td><td> The filename of this 
 * attachment </td></tr> 
 * <tr><td> path </td><td> String </td><td> The path to this attachment 
 * </td></tr> 
 * <tr><td> url </td><td> String </td><td> A url to this attachment 
 * </td></tr> 
 * <tr><td> size </td><td> Integer </td><td> The size of the file in bytes 
 * </td></tr> 
 * <tr><td> type </td><td> AttachmentType </td><td> The type of this 
 * attachment </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class AttachmentTable<T extends Attachment> extends AttachmentTableBase<Attachment> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public AttachmentTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here
}

