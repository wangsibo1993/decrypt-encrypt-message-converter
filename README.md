# decrypt-encrypt-message-converter
spring mvc 加解密报文
注解 @EncryptDecrypt 作用于类或者方法上，即实现加解密spring mvc报文
注解 @ExcludeEncryptDecrypt作用与方法 可在@EncryptDecrypt作用于类之后，排除不想加密的方法
注解 @RequestDecryptBody 代替@RequestBody 实现解密报文
注解 @ResponseEncryptBody 代替@ResponseBody 实现加密报文
