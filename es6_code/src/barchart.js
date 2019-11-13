import { Chart } from 'chart.js';
class BarChart {
    constructor(props) {
        this.template = `
        <div class="col-xs-12 col-md-4">
            <div class="bar_chart_card card">
                <h5>$TITLE</h5>   
                <canvas id="$ID" height="400" width="600"></canvas>
            </div>
        </div>
        `;
        console.log(props);
        this.id = props.id;
        this.title = props.title;
        this.dataSet = props.data;
        this.options = props.options;
    }
    render(parent) {
        if (!parent.querySelector(this.id)) {
            parent.insertAdjacentHTML('beforeend', this.template.replace('$ID', this.id).replace("$TITLE", this.title))
        }
        var ctx = parent.querySelector('#' + this.id),
            config = {
                type: 'bar',
                data: this.dataSet,
                options: this.options
            };
        //console.log('barchart -> ', ctx, this.id, config);
        this.barchart = new Chart(ctx, config);
    }
    update(data) {
        console.log('Updating barchart -> ', data, this.barchart);
        this.barchart.data = data;
        this.barchart.update();
    }
}
export default BarChart;