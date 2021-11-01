module.exports = {
  outputDir: 'dist',
  chainWebpack: config => {
    // 移除 prefetch 插件
    // config.plugins.delete('prefetch-index');
    config.externals({
      // vue: 'Vue',
      // 'vue-router': 'VueRouter',
      // 'lz-string': 'LZString',
      // axios: 'axios',
      // "phaser":"Phaser",
      "pixi.js": "PIXI",
      dragonBones: "dragonBones"
    });
    // config.plugins.delete('prefetch-waos')
    // config.plugins.delete('prefetch-minieco')
  },
}