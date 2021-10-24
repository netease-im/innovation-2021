import { deepCopy } from "../base";
import { TaskQueue } from "../TaskQueue";

type simpleFlowReturn<T = any> = [ValidFlowName, T, any?] | T | ValidFlowName;
type promiseFlowReturn<T = any> = Promise<simpleFlowReturn<T>>;

type ValidFlowHandleReturn<T = any> =
  | simpleFlowReturn<T>
  | promiseFlowReturn<T>;

type IFlowHandlerFunction<D1 = any, D2 = any> = (
  data: D1,
  next: (data?: any) => void,
  mark: (markName: SimpleValue) => void
) => any;
type IFlowConditionHandlerFunction = (data: any) => ValidFlowName;
type IFlowHandler = IFlowHandlerFunction | SimpleValue;
type IFlowConditionHandler = IFlowConditionHandlerFunction | SimpleValue;
interface IFlowHandlerConfig {
  handlers: IFlowHandler[];
  name: ValidFlowName;
  outputs: Map<SimpleValue, ValidFlowName>;
  conditions: Map<[Set<SimpleValue>, 1 | 0], IFlowConditionHandler[]>;
  config?: any;
}
type ValidFlowName = string | number | Symbol | boolean;
type SimpleValue = ValidFlowName | undefined | null;
type WhenAndFunction = (cn: SimpleValue) => WhenAndReturn;
type WhenOrFunction = (cn: SimpleValue) => WhenOrReturn;
type WhenExecFunction = (...args: IFlowConditionHandler[]) => FlowStepDep;
interface WhenAndReturn {
  and: WhenAndFunction;
  exec: WhenExecFunction;
}
interface WhenOrReturn {
  or: WhenOrFunction;
  exec: WhenExecFunction;
}

interface WhenReturn {
  and: WhenAndFunction;
  or: WhenOrFunction;
  exec: WhenExecFunction;
}

interface FlowStepDep {
  pipe: (...args: IFlowHandler[]) => FlowStepDep;
  case: (result: SimpleValue, flow: ValidFlowName) => FlowStepDep;
  when: (caseName: SimpleValue) => WhenReturn;
}

export class FlowCenter {
  private execQueue = new TaskQueue();
  private fmap = new Map<ValidFlowName, Set<IFlowHandlerConfig>>();
  on(flow: ValidFlowName, config?: { replay?: boolean }): FlowStepDep {
    const flowConfig: IFlowHandlerConfig = {
      handlers: [],
      name: flow,
      outputs: new Map(),
      conditions: new Map(),
      config,
    };
    const add = () => {
      let sets = this.fmap.get(flow);
      if (!sets) {
        this.fmap.set(flow, (sets = new Set()));
      }
      sets.add(flowConfig);
    };
    const FlowOnObj = {
      pipe: (...args: IFlowHandler[]) => {
        flowConfig.handlers.push(...args);
        add();
        return FlowOnObj;
      },
      case: (result: SimpleValue, flow: ValidFlowName) => {
        flowConfig.outputs.set(result, flow);
        return FlowOnObj;
      },
      when: (caseName: SimpleValue): WhenReturn => {
        const cases = new Set<SimpleValue>();
        cases.add(caseName);
        let mark: 0 | 1 = 1;
        const exec = (...args: IFlowConditionHandler[]) => {
          flowConfig.conditions.set([cases, mark], args);
          console.log(flowConfig.conditions);
          return FlowOnObj;
        };
        const andFunc = (cn: SimpleValue) => {
          cases.add(cn);
          return {
            and: andFunc,
            exec,
          };
        };
        const orFunc = (cn: SimpleValue) => {
          cases.add(cn);
          return {
            or: orFunc,
            exec,
          };
        };
        return {
          and: (cn: SimpleValue) => {
            mark = 1;
            return andFunc.call(null, cn);
          },
          or: (cn: SimpleValue) => {
            mark = 0;
            return orFunc.call(null, cn);
          },
          exec,
        };
      },
      drop: () => {
        const deps = this.fmap.get(flow);
        
        deps && deps.delete(flowConfig);
      },
    };
    return FlowOnObj;
  }
  private async notify(dep: IFlowHandlerConfig, data: any) {
    const marks = new Set<SimpleValue>();
    const { handlers, outputs, conditions } = dep;
    console.log({ dep, data });
    // 入口处理
    try {
      let goNext = false;
      function next(d: any) {
        goNext = true;
        data = d;
      }
      function mark(name: SimpleValue) {
        marks.add(name);
      }
      for (const handler of handlers) {
        goNext = false;
        marks.add(
          typeof handler === "function"
            ? await this.execQueue.exec<ValidFlowHandleReturn>(
                handler.bind(null, data, next, mark)
              )
            : handler
        );
        // handler next
        if (!goNext) {
          break;
        }
      }
    } catch (error) {
      console.error(error);
    }
    // 执行条件聚合
    conditions.forEach((handlers, key) => {
      const [set, type] = key;
      let prop;
      if (type === 0) {
        // or case
        if (Array.from(marks).some((el) => set.has(el))) {
          prop = true;
          console.log("prop or case ---------------");
        }
      } else if (type === 1) {
        if (Array.from(set).every((key) => marks.has(key))) {
          prop = true;
          console.log("prop and case ---------------");
        }
      }
      if (prop) {
        handlers.forEach(async (handler) => {
          const res =
            typeof handler === "function"
              ? await this.execQueue.exec<ValidFlowHandleReturn>(
                  handler.bind(null, data)
                )
              : handler;
          if (res !== undefined && res !== null) {
            this.delayTo(res, data);
          }
        });
      }
    });
    // 执行出口
    marks.forEach((mark) => {
      if (outputs.has(mark)) {
        // 出口处理
        console.log(`exec output ----------${outputs.get(mark)}---------`);
        this.delayTo(outputs.get(mark)!, data);
      }
    });
  }
  private delayTo(flow: ValidFlowName, data?: any, time = 0) {
    setTimeout(() => {
      this.to(flow, data);
    }, time);
  }
  to(flow: ValidFlowName, data?: any) {
    const flowDeps = this.fmap.get(flow);
    console.log(this.fmap);
    if (!flowDeps) return;
    return Promise.all(
      Array.from(flowDeps).map((el) => this.notify(el, deepCopy(data)))
    );
  }
}

function test(i: IFlowHandler) {
  console.log(i);
}
test((d) => {});
const fc = new FlowCenter();

fc.on("test")
  .pipe((data, next, mark) => {
    console.info("test handler");
    console.log(data);
    mark(true);
  })
  .case(true, "@test")
  .when(true)
  .exec("finished");

fc.on("@test")
  .pipe(
    (d, next) => {
      console.info("after test");
      next();
    },
    () => {
      console.log("after test 2");
      return true;
    }
  )

  .case(true, "finished");

fc.on("finished").pipe(() => {
  console.info("------------- finished ---------------");
});

fc.to("test");

// function test1(s:any,t:any){
//   console.log({s,t})
//   return s+1
// }
// let aa =1
// eval("console.log(test1`haha${aa}`)")