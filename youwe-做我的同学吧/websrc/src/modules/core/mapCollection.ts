import { onUnmounted, reactive } from "vue";

const defTrans = (doc) => doc;

export const makeCollectionReactive = (
  container: any[],
  collection: Mongo.Collection<any, any>,
  selector: any,
  tranform = defTrans,
  options?: any
) => {
  const q = collection.find(selector, options)
  q.fetch().forEach(el => container.push(tranform(el)))
  return q.observe({
    added(doc) {
      // console.log("add", doc);
      container.push(tranform(doc));
    },
    removed({ _id }) {
      // console.log("remove", _id);
      const i = container.findIndex((el) => el._id === _id);
      i > -1 && container.splice(i, 1);
    },
    changed(nd, { _id }) {
      // console.log("changed", _id, nd);
      const docIndex = container.findIndex((e) => e._id === _id);
      if (docIndex < 0) return;
      container.splice(docIndex, 1, tranform(nd));
    },
  });
}

export const makeCollectionItemReactive = function <T = any>(
  collection: Mongo.Collection<any, any>,
  selector: any,
  options?: any): T {
  const container = reactive(collection.findOne(selector) || {})
  const lq = collection.find(selector, options).observeChanges({
    added(_id, fields) {
      // console.log("add", doc);
      if (!container._id) {
        container._id = _id
        console.error(`add the one`)
      } else if (_id !== container._id) {
        console.error(`skip not the one`)
        return
      }
      for (const key in fields) {
        container[key] = fields[key]
      }
    },
    removed(_id) {
      if (_id !== container._id) {
        return
      }
      for (const key in container) {
        delete container[key]
      }
    },
    changed(_id, fields) {
      if (_id !== container._id) {
        return
      }
      for (const key in fields) {
        container[key] = fields[key]
      }
    },
  });
  onUnmounted(() => lq.stop())
  return container
}