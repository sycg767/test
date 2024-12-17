export const isExternal = (path: string) => /^(https?:|mailto:|tel:)/.test(path)

export const isValidUsername = (str: string) => str.length >= 3

export const isValidPassword = (str: string) => str.length >= 6 