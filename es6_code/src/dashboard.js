class Dashboard {
    constructor() {
        this.template = `
        <section id="dashboard" class="dashboard">
            <div class="row first_row flex_box_col">
                <h4 class="section_header col-xs-12">Overview</h5>
                <div class="donut_container flex_box col-xs-12" id="card_container">
                    <div class="card">
                        <canvas id="donut" height="400" width="600"></canvas>
                    </div>
                </div>
            </div>
            <div class="row second_row">
                <h4 class="section_header col-xs-12">Distribution</h5>
                <div id='bar_chart_container'>
                </div>
            </div>
        </section>
        `;
    }
    inject(b) {
        if (b) {
            b.insertAdjacentHTML('beforeend', this.template);
        }
    }
}
export default Dashboard;