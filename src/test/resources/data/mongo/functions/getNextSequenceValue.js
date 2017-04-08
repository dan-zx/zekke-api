function (name) {
  var ret = db.sequences.findAndModify(
      {
        query: { _id: name },
        update: { $inc: { seq: 1 } },
        new: true
      }
    );
  return NumberInt(ret.seq);
};
