class Template {
    static get cardTemplate() {
        return `
        <nav></nav>
        <main class="main" id="main">
            <header>
                <div class="profile"></div>
            </header>
            <section id="container"></section>
        </main>
        `;
    }
    static get errorTemplate() {
        return `
            <div class="global_error" id="error">
                <div class="error_box">
                    <i class="fas fa fa-warning"></i>
                    <p class="error_text">Oops! An error occurred :-(</p>
                </div>
            </div>
        `;
    }
    static get loadingTemplate() {
        return `
            <div class="global_loading lds-css" id="loading">
                <div class="lds-css ng-scope">
                    <div style="width:100%;height:100%" class="lds-rolling">
                        <div></div>
                    </div>
                </div>
            </div>
        `;
    }

}

export default Template;