function (name) {
  var sequenceDocument = db.sequences.findAndModify(
      {
        query: { _id: name },
        update: { $inc: { seq: 1 } },
        new: true
      }
    );
  return sequenceDocument.seq;
};
