package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class UserTable extends UserTableBase {

  public UserTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);

  }

  protected void init() throws PoemException {
    super.init();

    // We can't put this in the constructor because the email
    // column isn't defined until we've called UserTableBase.init()
    getEmailColumn().setRaw_unsafe(guestUser, "");
    getEmailColumn().setRaw_unsafe(administratorUser, "");
  }
}
