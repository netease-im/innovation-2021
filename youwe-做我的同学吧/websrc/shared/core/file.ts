import { hashString } from "./hash";

/**
 * 保存为本地文件
 * @param data
 * @param name
 */
export function saveString(data: string, name: string) {
  const urlObject = window.URL || window.webkitURL;
  const blob = new Blob([data]);
  const link = document.createElement("a");
  link.href = urlObject.createObjectURL(blob);
  link.download = name;
  const ev = document.createEvent("MouseEvents");
  ev.initMouseEvent(
    "click",
    true,
    false,
    window,
    0,
    0,
    0,
    0,
    0,
    false,
    false,
    false,
    false,
    0,
    null
  );
  link.dispatchEvent(ev);
}
export function saveJson(data: any, name: string) {
  saveString(JSON.stringify(data), `${name}.json`);
}

export async function chooseFile(
  type = "*",
  maxsize = 40
): Promise<string | null> {
  const file = await chooseFilePure(type, maxsize);
  if (!file) {
    return null;
  }
  return (await readFile(file)) as string;
}
export function chooseFilePure(type = "*", maxsize = 40): Promise<File | null> {
  return new Promise((res) => {
    const t = document.createElement("input");
    t.type = "file";
    t.accept = type;
    t.onchange = (e: any) => {
      // t.remove();
      if (e.target) {
        const file = e.target.files[0];
        if (!file) {
          return res(null);
        }
        if (file.size > maxsize * 1024) {
          alert("图片过大");
          return res(null);
        }
        res(file);
      }
    };
    t.click();
  });
}
export function readFile(
  file: File,
  type?: "text" | "buffer" | "string"
): Promise<string | ArrayBuffer | null> {
  return new Promise((res) => {
    if (!file) {
      return res(null);
    }
    const reader = new FileReader();
    reader.onload = () => {
      res(reader.result as string);
    };
    switch (type) {
      case "text":
        reader.readAsText(file);
        break;
      case "buffer":
        reader.readAsArrayBuffer(file);
        break;
      case "string":
        reader.readAsBinaryString(file);
        break;
      default:
        reader.readAsDataURL(file);
        break;
    }
  });
}
export function chooseFileUniqName(
  type = "*",
  maxsize = 40
): Promise<{ file: File; name: string; url: string } | null> {
  return new Promise((res) => {
    chooseFilePure(type, maxsize).then((file) => {
      if (!file) {
        return res(null);
      }
      const reader = new FileReader();
      reader.onload = () => {
        const url = reader.result as string;
        res({
          file,
          url,
          name: hashString(url),
        });
      };
      reader.readAsDataURL(file);
    });
  });
}

export function printeText(txt: string) {
  const oPrntWin = window.open(
    "",
    "_blank",
    "width=450,height=470,left=400,top=100,menubar=yes,toolbar=no,location=no,scrollbars=yes"
  );
  if (!oPrntWin) {
    return;
  }
  oPrntWin.document.open();
  const style = `section>div {margin: 0 0 10px 0;} #profile{position: relative;}
	section { border-bottom: 1px solid rgba(0, 0, 0, 0.1); padding: 20px 0;}
	li {line-height: 2em;	margin-bottom: 1em;list-style-type: none;}
	.skill-list { 	display: flex; justify-content: space-between;}
.skill-list li {	display: inline-block;}`;
  oPrntWin.document.write(
    "<!doctype html><html><head><style>" +
      style +
      '</style><title>Print</title></head><body onload="print();">' +
      txt +
      "</body></html>"
  );
  oPrntWin.document.close();
}
