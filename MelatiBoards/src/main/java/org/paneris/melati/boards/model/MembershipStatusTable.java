package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class MembershipStatusTable extends MembershipStatusTableBase {

  private MembershipStatus normal, digest, suspended;

  public MembershipStatusTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  protected void postInitialise() {
    super.postInitialise();

    normal = ensure("normal");
    digest = ensure("digest");
    suspended = ensure("suspended");

    TableInfo info = getInfo();
    if (info.getDefaultcanwrite() == null)
      info.setDefaultcanwrite(getDatabase().administerCapability());
    if (info.getCancreate() == null)
      info.setCancreate(getDatabase().administerCapability());
  }

  public MembershipStatus getNormal() {
    return normal;
  }

  public MembershipStatus getDigest() {
    return digest;
  }

  public MembershipStatus getSuspended() {
    return suspended;
  }

  public MembershipStatus ensure(String name) {
    MembershipStatus status =
                    (MembershipStatus)getStatusColumn().firstWhereEq(name);
    if (status != null)
      return status;
    else {
      status = (MembershipStatus) newPersistent();
      status.setStatus(name);
      return (MembershipStatus)getStatusColumn().ensure(status);
    }
  }

}
