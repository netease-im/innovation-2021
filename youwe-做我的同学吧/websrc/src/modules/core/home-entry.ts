export const RoomEntryTypes = new Map<
  string,
  { component: any; name: string }
>();

export const registEntryType = (key, name, component) => {
  RoomEntryTypes.set(key, { component, name });
};

