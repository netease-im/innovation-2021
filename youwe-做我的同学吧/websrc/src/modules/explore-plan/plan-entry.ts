export const RoomEntryTypes = new Map();

export const registEntryType = (key, name, component) => {
  RoomEntryTypes.set(key, { component, name });
};
