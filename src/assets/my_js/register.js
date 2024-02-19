const checkUser = (rule, value, callback) => {
    const user_success = /^[0-9]+/; // 11位学号
    const user_len = /[0-9]{11}/;
    // 异步通讯检测学工号存在，参数列表存疑
    if (value.match(user_success) && value.match(user_len)) {
        this.ruleForm.email = value + "@fudan.edu.cn";
        return this.$axios.post(
            '/register/username',
            {user: this.ruleForm.username})
            .then(resp => {
                if (resp.status == 200)
                    return callback();
                else
                    return callback(new Error('后端数据库返回值无法识别'));
            }).catch(error => {
                if (error.response.status == 400)
                    return callback(new Error('学工号已存在！'));
                else if (error.response.status == 403 || error.response.status == 500) {
                    return callback(new Error('学工号非法！'));
                } else {
                    console.log(error);
                    return callback(new Error('后端数据库返回值无法识别'));
                }
            });

    }

    return callback(new Error('学工号为11位数字'));
};

const checkPassword = (rule, value, callback) => {
    const REG_NEW = /^[A-z0-9\-_]{6,32}$/; // 字⺟，数字或者特殊字符（-_）
    // 至少两种==不能只有一种
    const REG_NUM = /^[0-9]{6,32}$/; // 只有数字若干
    const REG_CHAR = /^[A-z]{6,32}$/; // 只有字母若干
    const REG_SPEC = /^[_\-]{6,32}$/; // 只有-和_若干
    const REG_NUM2 = /[0-9]/; // 存在数字
    const REG_CHAR2 = /[A-z]/; // 存在字母
    const REG_SPEC2 = /[_\-]/; // 存在-和_

    if (!value.match(REG_NEW)) {
        return callback(new Error("无效密码：6-32位字⺟，数字或者特殊字符（-_）⾄少包含两种"));
    }

    // 不能包含账号
    if (this.ruleForm.username !== '' &&
        value.toString().includes(this.ruleForm.username.toString()))
        return callback(new Error("密码不能包含账号"));

    // '字⺟，数字或者特殊字符（-_）⾄少包含两种';
    if (value.match(REG_NEW) && !value.match(REG_NUM) && !value.match(REG_CHAR)
        && !value.match(REG_SPEC)) {
        // 合法密码：中强度密码
        this.percentage = 67;
        // 字母数字特殊符号都存在-强密码
        if (value.match(REG_NUM2) && value.match(REG_CHAR2) && value.match(REG_SPEC2))
            this.percentage = 100;
        return callback();
    } else {
        // 非法字符密码
        if (!value.match(REG_NEW)) {
            this.percentage = 0;
            return callback(new Error("非法字符：字⺟，数字或者特殊字符（-_）⾄少包含两种"))
        } else if (value.match(REG_NUM) || value.match(REG_CHAR) || value.match(REG_SPEC)) {
            // 无效低强度密码
            this.percentage = 33;
        }
        return callback(new Error("弱密码：字⺟，数字或者特殊字符（-_）⾄少包含两种"));
    }
};

const checkPassword2 = (rule, value, callback) => {
    if (value === this.ruleForm.password) return callback();
    else return callback(new Error("两次输入的密码不一致"));
};

const checkEmail = (rule, value, callback) => {
    this.isExist = null;
    const REG_EMAIL = /^([a-z]|[A-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+((\.[a-zA-Z]{2,4}){1,2})$/;
    if (value === '') {
        asyn_post_email(rule, value, callback);
        return callback(new Error("邮箱不能为空"));
    } else if (value.match(REG_EMAIL)) {
        // 异步通讯检测邮箱存在性
        return this.$axios.post(
            '/register/email',
            {email: this.ruleForm.email})
            .then(resp => {
                if (resp.status == 200)
                    return callback();

                return callback(new Error('后端数据库返回值无法识别'));
            }).catch(error => {
                if (error.response.status == 403) {
                    return callback(new Error('邮箱格式错误！'));
                }
                if (error.response.status == 400)// 0 不存在 1 存在
                    return callback(new Error('邮箱已被注册！'));
                else {
                    console.log(error);
                    return callback(new Error('后端数据库返回值无法识别'));
                }
            });

    }
    return callback(new Error("错误的邮箱格式！"));
};