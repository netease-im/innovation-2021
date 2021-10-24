const {execSync} = require("child_process")

exports.build =  () =>{
    execSync("npm run build:vue")
    execSync("ionic cap copy")
    // execSync("ionic cap sync")
    execSync("cd android && .\\gradlew :app:assembleDebug")
}



// gradlew :app:assembleDebug # 编译 debug 包，当前用的是多渠道编译所以没有使用参数的话会编译所有渠道的debug包
// gradlew :app:assembleRelease # 编译 release 包，当前用的是多渠道编译所以没有使用参数的话会编译所有渠道的release包
 
// gradlew :app:assembleBaiduDebug # debug包，指定编译百度渠道的debug包
// gradlew :app:assembleYingyongbaoDebug # debug包，指定编译应用宝渠道的debug包
// gradlew :app:assembleBaiduRelease # # release包，指定编译百度渠道的release包
// gradlew :app:assembleDebugRelease # release包，指定编译应用宝渠道的release包

exports.api = function(){
    execSync('cd server && meteor run')
}