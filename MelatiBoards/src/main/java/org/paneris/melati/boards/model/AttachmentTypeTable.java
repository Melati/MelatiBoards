package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class AttachmentTypeTable extends AttachmentTypeTableBase {

  public AttachmentTypeTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public AttachmentType ensure(String contentType) {
    AttachmentType newType = (AttachmentType)newPersistent();
    newType.setType_unsafe(contentType);
    return (AttachmentType)getColumn("type").ensure(newType);
  }
}
