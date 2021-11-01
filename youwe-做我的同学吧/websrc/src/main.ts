import "./meteor";
import { appUse } from "./modules/core/app";
import User from "./modules/user";
appUse(User);
import Ionic from "./ionic";
appUse(Ionic);
import ExplorePlan from "./modules/explore-plan";
appUse(ExplorePlan);
import PoemCard from "./modules/poemcard";
appUse(PoemCard)
import OperaPiyin from "./modules/operastage";
appUse(OperaPiyin)
