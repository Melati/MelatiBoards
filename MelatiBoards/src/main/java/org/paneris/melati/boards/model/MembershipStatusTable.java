package org.paneris.melati.boards.model;


import org.paneris.melati.boards.model.generated.MembershipStatusTableBase;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.PoemException;
import org.melati.poem.TableInfo;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>MembershipStatusTable</code> object.
 * <p>
 * Description: 
 *   The status of a user's subscription to a board. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>MembershipStatus</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> status </td><td> String </td><td> The name of a status </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class MembershipStatusTable<T extends MembershipStatus> extends MembershipStatusTableBase<MembershipStatus> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public MembershipStatusTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here
  

  private MembershipStatus normal, digest, suspended, banned;

  public void postInitialise() {
    super.postInitialise();

    normal = ensure("normal");
    digest = ensure("digest");
    suspended = ensure("suspended");
    banned = ensure("banned");

    TableInfo info = getInfo();
    if (info.getDefaultcanwrite() == null)
      info.setDefaultcanwrite(getDatabase().administerCapability());
    if (info.getCancreate() == null)
      info.setCancreate(getDatabase().administerCapability());
  }

 /**
  * @return the Normal MembershipStatus.
  */
  public MembershipStatus getNormal() {
    return normal;
  }

 /**
  * @return the Digest MembershipStatus.
  */
  public MembershipStatus getDigest() {
    return digest;
  }

 /**
  * @return the Suspended MembershipStatus.
  */
  public MembershipStatus getSuspended() {
    return suspended;
  }

 /**
  * @return the Banned MembershipStatus.
  */
  public MembershipStatus getBanned() {
    return banned;
  }

 /**
  * Make sure that a record exists.
  *
  * @param name   of the Membershipstatus to ensure.
  * @return the existing or newly created MembershipStatus of given name.
  */
  public MembershipStatus ensure(String name) {
    MembershipStatus status =
                    (MembershipStatus)getStatusColumn().firstWhereEq(name);
    if (status != null)
      return status;
    status = (MembershipStatus) newPersistent();
    status.setStatus(name);
    return (MembershipStatus)getStatusColumn().ensure(status);
  }


}

