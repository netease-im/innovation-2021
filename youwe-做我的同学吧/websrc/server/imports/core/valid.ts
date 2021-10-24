export function validUsername(str: string) {
  if (typeof str !== 'string' || str.length < 6 || str.length > 16) {
    return false;
  }
  return /^[a-z0-9]+$/i.test(str);
}
export function validNickname(str: string) {
  if (typeof str !== 'string' || str.length < 1 || str.length > 12) {
    return false;
  }
  return /^[\u4e00-\u9fa5A-Za-z0-9-_]*$/.test(str);
}
