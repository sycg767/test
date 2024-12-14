import { ref } from 'vue'

export const useTheme = () => {
  // 主题色
  const primaryColor = ref('#1890ff')

  // 设置主题色
  const setPrimaryColor = (color: string) => {
    primaryColor.value = color
    // 更新CSS变量
    document.documentElement.style.setProperty('--el-color-primary', color)
    
    // 生成不同色阶
    const colors = generateColors(color)
    Object.entries(colors).forEach(([key, value]) => {
      document.documentElement.style.setProperty(`--el-color-primary-${key}`, value)
    })
  }

  // 生成不同色阶
  const generateColors = (primary: string) => {
    const colors = {
      'light-3': mix(primary, '#ffffff', 0.3),
      'light-5': mix(primary, '#ffffff', 0.5),
      'light-7': mix(primary, '#ffffff', 0.7),
      'light-8': mix(primary, '#ffffff', 0.8),
      'light-9': mix(primary, '#ffffff', 0.9),
      'dark-2': mix(primary, '#000000', 0.2)
    }
    return colors
  }

  // 混合颜色
  const mix = (color1: string, color2: string, weight: number) => {
    weight = Math.max(Math.min(Number(weight), 1), 0)
    const r1 = parseInt(color1.substring(1, 3), 16)
    const g1 = parseInt(color1.substring(3, 5), 16)
    const b1 = parseInt(color1.substring(5, 7), 16)
    const r2 = parseInt(color2.substring(1, 3), 16)
    const g2 = parseInt(color2.substring(3, 5), 16)
    const b2 = parseInt(color2.substring(5, 7), 16)
    const r = Math.round(r1 * (1 - weight) + r2 * weight)
    const g = Math.round(g1 * (1 - weight) + g2 * weight)
    const b = Math.round(b1 * (1 - weight) + b2 * weight)
    return `#${((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1)}`
  }

  return {
    primaryColor,
    setPrimaryColor
  }
} 