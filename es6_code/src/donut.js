import { Chart } from 'chart.js';
class Donut {
    constructor(ctx, dataSet, options) {
        this.ctx = ctx;
        this.dataSet = dataSet;
        this.options = options;
    }
    render() {
        this.donut = new Chart(this.ctx, {
            type: 'doughnut',
            data: this.dataSet,
            options: this.options
        });
    }
    update(datasets) {
        console.log('data to update -> ', datasets);
        this.donut.data.datasets = datasets;
        this.donut.update();
    }
}
export default Donut;