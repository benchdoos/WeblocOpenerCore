/*
 * (C) Copyright 2019.  Eugene Zrazhevsky and others.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 * Eugene Zrazhevsky <eugene.zrazhevsky@gmail.com>
 */

package com.github.benchdoos.weblocopenercore.core.constants;

public interface StringConstants {
    String UPDATE_WEB_URL = "https://benchdoos.github.io/";
    String GITHUB_WEB_URL = "https://github.com/benchdoos/WeblocOpener/";
    String GITHUB_REPORT_ISSUE = "https://github.com/benchdoos/WeblocOpener/issues/new/choose";
    String GITHUB_NAME = "Github";

    String BENCH_DOOS_TELEGRAM_URL = "https://vk.cc/74nB3D"; //for stats
    String WEBLOCOPENER_TWITTER_URL = "https://vk.cc/8YubRk"; //for stats
    String DONATE_PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=eugeny.zrazhevsky@icloud.com&item_name=Donation+for+WeblocOpener&cy_code=USD";
    String FEEDBACK_MAIL_URL = "mailto:weblocopener@gmail.com?subject=WeblocOpener%20feedback" +
            "&body=Type%20here%20your%20question%20/%20problem,%20I%20try%20to%20help%20you%20as%20soon%20as%20it%20is%20possible!" +
            "%0AYou%20can%20attach%20log%20files%20(see%20WeblocOpener%20-%20Settings%20-%20About%20-%20Log%20folder%20-%20zip%20log%20folder%20-%20attach)." +
            "%0ADon't%20forget%20to%20close%20the%20application%20before%20zipping%20logs;)";
    String DONATE_DONATION_ALERTS_URL = "https://www.donationalerts.com/r/benchdoos";
    String WINDOWS_WEBLOCOPENER_SETUP_NAME = "WeblocOpenerSetupV";
    String FAVICON_GETTER_URL = "https://besticon-demo.herokuapp.com/allicons.json?url=";

    //-----
    String SHARE_USER_INFO_URL = "https://weblocopener-statistics.herokuapp.com/application-login/notify/%s";
    String SEND_FEEDBACK_URL = "https://weblocopener-statistics.herokuapp.com/feedback";

    String SHARE_USER_INFO_DEV_MODE_URL = "http://localhost:8022/application-login/notify/%s";
    String SEND_FEEDBACK_DEV_MODE_URL = "http://localhost:8022/feedback";
    //-----

    String IMGBB_API_KEY = "71b8d5b5275a7aae78efc21d55be19bf";
}
